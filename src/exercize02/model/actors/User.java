package exercize02.model.actors;

import akka.actor.AbstractActor;
import exercize02.model.messages.SendMsg;
import javafx.application.Platform;

public class User extends AbstractActor {

    private final String name;

    public User(final String name) {
        this.name = name;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SendMsg.class, msg -> {
            //TODO cosa fare quando si invia un messaggio Broadcast.
            Platform.runLater(() -> {
                msg.getListOfMessages().add(msg.getMessage());
            });
        }).build();
    }

    public String getName() {
        return this.name;
    }
}
