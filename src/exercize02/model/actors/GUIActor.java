package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import exercize02.model.messages.*;
import exercize02.model.utility.ActorsUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import javax.swing.text.html.ListView;
import java.util.List;
import java.util.Map;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;
    private final Map<ActorRef, ObservableList<String>> mapOfChats;

    public GUIActor(final ObservableList<ActorRef> users, final Map<ActorRef, ObservableList<String>> mapOfChats) {
        this.users = users;
        this.mapOfChats = mapOfChats;
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SendButtonMessageMsg.class, msg -> {
            msg.getSender().tell(new SendMsg(msg.getMessage(), msg.getListOfMessages()), getSender());
        }).match(AddActorButtonPressedMsg.class, msg -> {
            Platform.runLater(() ->
                    this.users.add(getContext().getSystem().actorOf(
                            Props.create(User.class, ActorsUtility.generateActorName()),
                            ActorsUtility.generateActorName()))
            );
        }).match(RemActorButtonPressedMsg.class, msg -> {
            Platform.runLater(() -> this.users.remove(msg.getToBeRemoved()));
        }).match(GUIShowMsg.class, msg -> {
            Platform.runLater(() -> this.mapOfChats.get(getSender()).add(msg.getMsg()));
            msg.getSender().tell(new GUIAcknowledgeMsg(msg.getMsg(), msg.getSender()), getSelf());
        }).match(ActorSelectedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene selezionato un attore dalla Lista.
        }).build();
    }
}
