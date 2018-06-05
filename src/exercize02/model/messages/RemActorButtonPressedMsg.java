package exercize02.model.messages;

import akka.actor.ActorRef;

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
