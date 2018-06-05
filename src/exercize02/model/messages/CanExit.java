package exercize02.model.messages;

import akka.actor.ActorRef;

public class CanExit {
    private final ActorRef removeWho;

    public CanExit(final ActorRef whoSends) {
        this.removeWho = whoSends;
    }

    public CanExit(){
        this.removeWho = null;
    }

    public ActorRef getToBeRemoved() {
        return this.removeWho;
    }

}
