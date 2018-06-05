package exercize02.view;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize02.model.actors.GUIActor;
import exercize02.model.messages.*;
import javafx.scene.control.*;

public class ViewController {
    public ListView<String> listOfMessages;
    public ListView<ActorRef> actorsList;
    public TextArea textArea;
    public Label labelActorInfo;
    public Button sendButton;
    public Button addButton;
    public Button removeButton;

    private ActorRef guiActor;

    public void initialize() {
        this.guiActor = ActorSystem.create("MySystem").actorOf(Props.create(GUIActor.class,
                this.actorsList.getItems()));
        sendButton.setDisable(true);
        removeButton.setDisable(true);
        this.setUpListView();
        this.setButtonsListeners();
    }

    private String getTextFromArea() {
        return this.textArea.getText() == null ? "No-msg" : this.textArea.getText();
    }

    private void setButtonsListeners() {
        this.addButton.setOnAction(e -> {
            guiActor.tell(new AddActorButtonPressedMsg(this.actorsList.getItems()), ActorRef.noSender());
        });
    }

    private void setUpListView() {
        this.actorsList.setCellFactory(lst -> new ListCell<ActorRef>() {
            @Override
            protected void updateItem(ActorRef item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.path().name());
                }
            }
        });

        this.actorsList.setOnMouseClicked( ev -> {
            this.guiActor.tell(new ActorSelectedMsg(), ActorRef.noSender());
            this.sendButton.setDisable(false);
            this.removeButton.setDisable(false);
            this.sendButton.setOnAction( add -> {
                this.invokeGuiActorForSendMsg(
                        this.actorsList.getSelectionModel().getSelectedItem(),
                        this.getTextFromArea());
            });

            this.removeButton.setOnAction(rem -> {
                this.invokeGuiActorForRemoveActor(this.actorsList.getSelectionModel().getSelectedItem());
                this.removeButton.setDisable(true);
            });
        });
    }

    private void invokeGuiActorForSendMsg(final ActorRef selectedActor, final String stringToSend) {
        guiActor.tell(new SendButtonMessageMsg(selectedActor, stringToSend, this.listOfMessages.getItems()),
                ActorRef.noSender());
    }
    private void invokeGuiActorForRemoveActor(final ActorRef selectedActor) {
        guiActor.tell(new RemActorButtonPressedMsg(selectedActor), ActorRef.noSender());
    }
}
