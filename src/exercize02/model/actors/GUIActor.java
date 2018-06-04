package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import exercize02.model.messages.*;
import exercize02.model.utility.ActorsUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;

    public GUIActor(final ObservableList<ActorRef> users) {
        this.users = users;
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
            //TODO cosa fare quando nella GUI viene premuto il bottone Remove.
        }).match(ActorSelectedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene selezionato un attore dalla Lista.
        }).build();
    }
}
