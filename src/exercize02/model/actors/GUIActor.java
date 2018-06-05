package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import exercize02.model.messages.*;
import exercize02.model.utility.ActorsUtility;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;
    private Map<ActorRef, ObservableList<String>> mapOfChats;

    public GUIActor(final ObservableList<ActorRef> users, final Map<ActorRef, ObservableList<String>> mapOfChats) {
        this.users = users;
        this.mapOfChats = mapOfChats;
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SendButtonMessageMsg.class, msg -> {
            msg.getSender().tell(new SendMsg(msg.getMessage(), msg.getListOfMessages()), getSender());
        }).match(AddActorButtonPressedMsg.class, msg -> {
            Platform.runLater(() -> {
                final ActorRef newActor = getContext().getSystem().actorOf(
                        Props.create(User.class, ActorsUtility.generateActorName()),
                        ActorsUtility.generateActorName());
                this.mapOfChats.putIfAbsent(newActor, FXCollections.observableArrayList());

                ///////////////
                System.out.println(mapOfChats);
                ///////////////

                this.users.add(newActor);
            });
        }).match(RemActorButtonPressedMsg.class, msg -> {
            Platform.runLater(() -> this.users.remove(msg.getToBeRemoved()));
        }).match(GUIShowMsg.class, msg -> {

            //TODO NON VIENE RAGGIUNTO QUESTO PUNTO!!!
            Platform.runLater(() -> this.mapOfChats.get(getSender()).add(msg.getMsg()));

            ///////////////////
            System.out.println(this.mapOfChats.get(getSender()));
            ////////////////////

            msg.getSender().tell(new GUIAcknowledgeMsg(msg.getMsg(), msg.getSender()), getSelf());
        }).match(ActorSelectedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene selezionato un attore dalla Lista.
        }).build();
    }
}
