package exercize02.model.messages;

import akka.actor.ActorRef;

import java.util.List;

/**
 * Richiesta di invio di un messaggio in broadcast agli altri attori presenti.
 */
public class SendBroadcastMsg {

    private final String msg;
    private final List<ActorRef> actors;

    public SendBroadcastMsg(final String msg, final List<ActorRef> actorRefs) {
        this.msg = msg;
        this.actors = actorRefs;
    }

    /**
     * Messaggio
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Attori a cui inviare il messaggio
     * @return
     */
    public List<ActorRef> getActors() {
        return actors;
    }
}
