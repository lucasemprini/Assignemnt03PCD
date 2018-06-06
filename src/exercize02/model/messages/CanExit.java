package exercize02.model.messages;

import akka.actor.ActorRef;

/**
 * Notifica la possibilità di uscire da parte di un attore dalla chat
 */
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
