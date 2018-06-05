package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize01.model.messages.StartSystemMsg;
import exercize02.model.messages.*;
import exercize02.model.utility.ActorsUtility;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;
    private Map<ActorRef, ObservableList<String>> mapOfChats;
    private final ActorRef registry;

    public GUIActor(final ObservableList<ActorRef> users, final Map<ActorRef, ObservableList<String>> mapOfChats) {
        this.users = users;
        this.mapOfChats = mapOfChats;
        registry = ActorSystem.create("MySystem").actorOf(Props.create(Register.class));
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
                registry.tell(new AddActorButtonPressedMsg(), newActor);
                newActor.tell(new StartUser(registry, getSelf()), ActorRef.noSender());

                this.mapOfChats.putIfAbsent(newActor, FXCollections.observableArrayList());

                ///////////////
                //System.out.println(mapOfChats);
                ///////////////

                this.users.add(newActor);
                if (users.size() == 1) registry.tell(new PassToken(), ActorRef.noSender());
            });
        }).match(RemActorButtonPressedMsg.class, msg -> {
            registry.tell(new RemActorButtonPressedMsg(msg.getToBeRemoved()), getSelf());
        }).match(CanExit.class, canExit -> {
            Platform.runLater(() -> this.users.remove(canExit.getToBeRemoved()));
        }).match(GUIShowMsg.class, msg -> {

            //TODO NON VIENE RAGGIUNTO QUESTO PUNTO!!!
            Platform.runLater(() -> this.mapOfChats.get(getSender()).add(msg.getMsg()));

            ///////////////////
            //System.out.println(this.mapOfChats.get(getSender()));
            ////////////////////

            getSender().tell(new GUIAcknowledgeMsg(msg.getMsg(), msg.getSender()), getSelf());
        }).match(ActorSelectedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene selezionato un attore dalla Lista.
        }).build();
    }
}
