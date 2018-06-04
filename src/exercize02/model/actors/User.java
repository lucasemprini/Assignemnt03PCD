package exercize02.model.actors;

import akka.actor.AbstractActor;
import exercize02.model.messages.SendMessageMsg;

public class User extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SendMessageMsg.class, msg -> {
            //TODO cosa fare quando si invia un messaggio Broadcast.
        }).build();
    }
}
