package exercize02.view;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize02.model.actors.GUIActor;
import exercize02.model.messages.*;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;

public class ViewController {
    public ListView<String> listOfMessages;
    public ListView<ActorRef> actorsList;
    public TextArea textArea;
    public Label labelActorInfo;
    public Button sendButton;
    public Button addButton;
    public Button removeButton;

    private final Map<ActorRef, ObservableList<String>> listOfChats = new HashMap<>();
    private ActorRef guiActor;

    public void initialize() {
        this.guiActor = ActorSystem.create("MySystem").actorOf(Props.create(GUIActor.class,
                this.actorsList.getItems(), this.listOfChats));

        sendButton.setDisable(true);
        removeButton.setDisable(true);
        textArea.setDisable(true);
        this.setUpListView();
        this.invokeGuiActorForAddActor();
    }

    private String getTextFromArea() {
        return this.textArea.getText() == null ? "No-msg" : this.textArea.getText();
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
            final ActorRef currentActor = this.actorsList.getSelectionModel().getSelectedItem();
            this.guiActor.tell(new ActorSelectedMsg(), ActorRef.noSender());
            this.sendButton.setDisable(false);
            this.removeButton.setDisable(false);
            this.textArea.setDisable(false);
            this.listOfMessages.setItems(this.listOfChats.get(currentActor));

            ////////////
            System.out.println(listOfMessages.getItems());
            //////////////

            this.sendButton.setOnAction( add -> {
                this.invokeGuiActorForSendMsg(currentActor,
                        this.getTextFromArea());
                this.sendButton.setDisable(true);
                this.textArea.clear();
                this.textArea.setDisable(true);
            });

            this.removeButton.setOnAction(rem -> {
                this.invokeGuiActorForRemoveActor(this.actorsList.getSelectionModel().getSelectedItem());
                this.removeButton.setDisable(true);
                this.sendButton.setDisable(true);
                this.textArea.setDisable(true);
            });
        });
    }

    private void invokeGuiActorForSendMsg(final ActorRef selectedActor, final String stringToSend) {
        guiActor.tell(new SendButtonMessageMsg(selectedActor, stringToSend, this.listOfChats.get(selectedActor)),
                ActorRef.noSender());
    }

    private void invokeGuiActorForRemoveActor(final ActorRef selectedActor) {
        guiActor.tell(new RemActorButtonPressedMsg(selectedActor),
                ActorRef.noSender());
    }

    private void invokeGuiActorForAddActor() {
        this.addButton.setOnAction(e -> {
            guiActor.tell(new AddActorButtonPressedMsg(this.actorsList.getItems()),
                    ActorRef.noSender());
        });
    }
}
