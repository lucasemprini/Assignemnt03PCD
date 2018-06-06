package exercize02.model.messages;

import akka.actor.ActorRef;

/**
 * Notifica la volontà di un attore di uscire dalla chat.
 */
public class RemActorButtonPressedMsg {

    private final ActorRef removeWho;

    public RemActorButtonPressedMsg(final ActorRef whoSends) {
        this.removeWho = whoSends;
    }

    public RemActorButtonPressedMsg(){
        this.removeWho = null;
    }

    public ActorRef getToBeRemoved() {
        return this.removeWho;
    }

}
