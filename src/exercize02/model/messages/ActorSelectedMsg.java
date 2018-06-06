package exercize02.model.messages;

import akka.actor.ActorRef;

public class ActorSelectedMsg {

    private final ActorRef selected;
    public ActorSelectedMsg(final ActorRef selected) {
        this.selected = selected;
    }

    public ActorRef getSelected() {
        return selected;
    }
}
