package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import exercize02.model.messages.ActorSelectedMsg;
import exercize02.model.messages.AddActorButtonPressedMsg;
import exercize02.model.messages.RemActorButtonPressedMsg;
import exercize02.model.messages.SendButtonPressedMsg;
import javafx.collections.ObservableList;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;

    public GUIActor(final ObservableList<ActorRef> users) {
        this.users = users;
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SendButtonPressedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene premuto il bottone Send.
        }).match(AddActorButtonPressedMsg.class, msg -> {

            //TODO cosa fare quando nella GUI viene premuto il bottone Add.
        }).match(RemActorButtonPressedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene premuto il bottone Remove.
        }).match(ActorSelectedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene selezionato un attore dalla Lista.
        }).build();
    }
}
