package exercize02.view;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize02.model.actors.GUIActor;
import exercize02.model.messages.ActorSelectedMsg;
import exercize02.model.messages.AddActorButtonPressedMsg;
import exercize02.model.messages.RemActorButtonPressedMsg;
import exercize02.model.messages.SendButtonMessageMsg;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ViewController {
    private final Map<ActorRef, ObservableList<String>> mapOfChats = new HashMap<>();
    public ListView<String> listOfMessages;
    public ListView<ActorRef> actorsList;
    public TextArea textArea;
    public Label labelActorInfo;
    public Button sendButton;
    public Button addButton;
    public Button removeButton;
    private ActorRef guiActor;

    private static final String NO_MSG_STRING = "I don't wanna talk";
    private int addCounter = 0;

    public void initialize() {
        this.guiActor = ActorSystem.create("MySystem").actorOf(Props.create(GUIActor.class,
                this.actorsList.getItems(), this.mapOfChats,
                this.listOfMessages.getItems(), this.labelActorInfo));

        sendButton.setDisable(true);
        removeButton.setDisable(true);
        textArea.setDisable(true);
        this.setUpListView();
        this.addButton.setOnAction(e -> this.setDialogWindow());
    }

    private void setDialogWindow() {
        TextInputDialog dialog = new TextInputDialog("NewActor" +
                (this.addCounter == 0 ? "" : this.addCounter));
        this.addCounter++;
        dialog.setTitle("Name selection");
        dialog.setHeaderText("What's your name?");
        dialog.setContentText("Name:");
        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(280, 120);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::invokeGuiActorForAddActor);
    }

    private String getTextFromArea() {
        return this.textArea.getText().isEmpty() ? NO_MSG_STRING : this.textArea.getText();
    }

    private void setUpListView() {
        this.actorsList.setCellFactory(lst -> new ListCell<>() {
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

        this.actorsList.setOnMouseClicked(ev -> {
            final ActorRef currentActor = this.actorsList.getSelectionModel().getSelectedItem();
            if(!this.actorsList.getItems().isEmpty() && currentActor != null) {
                this.invokeGuiActorForSelectedActor(currentActor);
                this.sendButton.setDisable(false);
                this.removeButton.setDisable(false);
                this.textArea.setDisable(false);
                this.listOfMessages.setItems(this.mapOfChats.get(currentActor));

                this.sendButton.setOnAction(add -> {
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
            }
        });
    }

    private void invokeGuiActorForSelectedActor(final ActorRef selectedActor) {
        this.guiActor.tell(new ActorSelectedMsg(selectedActor), ActorRef.noSender());
    }

    private void invokeGuiActorForSendMsg(final ActorRef selectedActor, final String stringToSend) {
        guiActor.tell(new SendButtonMessageMsg(
                selectedActor, stringToSend, this.mapOfChats.get(selectedActor)), ActorRef.noSender()
        );
    }

    private void invokeGuiActorForRemoveActor(final ActorRef selectedActor) {
        guiActor.tell(new RemActorButtonPressedMsg(selectedActor), ActorRef.noSender());
    }

    private void invokeGuiActorForAddActor(final String actorName) {
        guiActor.tell(new AddActorButtonPressedMsg(this.actorsList.getItems(), actorName), ActorRef.noSender());
    }
}
