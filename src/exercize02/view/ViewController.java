package exercize02.view;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.dsl.Creators;
import exercize01.model.messages.StartSystemMsg;
import exercize02.model.actors.GUIActor;
import exercize02.model.actors.User;
import exercize02.model.messages.*;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

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

    private void setButtonsListeners() {
        this.addButton.setOnAction(e -> {
            guiActor.tell(new AddActorButtonPressedMsg(this.actorsList.getItems()), ActorRef.noSender());
        });
        this.removeButton.setOnAction(e -> {
            guiActor.tell(new RemActorButtonPressedMsg(), ActorRef.noSender());
        });
    }

    private void setUpListView() {
        this.actorsList.setOnMouseClicked( ev -> {
            this.guiActor.tell(new ActorSelectedMsg(), ActorRef.noSender());
        });
    }

    private String getTextFromArea() {
        return this.textArea.getText();
    }

    private void invokeGuiActorForSendMsg() {
        guiActor.tell(new SendMessageMsg(this.getTextFromArea()),
                ActorRef.noSender());
    }
}
