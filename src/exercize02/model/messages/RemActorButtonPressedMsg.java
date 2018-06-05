package exercize02.model.messages;

import akka.actor.ActorRef;
import javafx.collections.ObservableList;

public class RemActorButtonPressedMsg {

    private final ActorRef removeWho;

    public RemActorButtonPressedMsg(final ActorRef whoSends) {
        this.removeWho = whoSends;
    }

    public ActorRef getToBeRemoved() {
        return this.removeWho;
    }

}
