package exercize02.model.messages;

import akka.actor.ActorRef;
/**
 * Notifica che un attore è stato selezionato.
 */
public class ActorSelectedMsg {

    private final ActorRef selected;
    public ActorSelectedMsg(final ActorRef selected) {
        this.selected = selected;
    }

    public ActorRef getSelected() {
        return selected;
    }
}
