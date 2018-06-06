package exercize02.model.messages;

import akka.actor.ActorRef;

/**
 * Ricevuta di ritorno da parte della GUI all'attore che gli ha chiesto di renderizzare un messaggio.
 */
public class GUIAcknowledgeMsg {
    private final String msg;
    private final ActorRef sender;

    public GUIAcknowledgeMsg(final String msg, final ActorRef sender) {
        this.msg = msg;
        this.sender = sender;
    }

    /**
     * Messaggio visualizzato
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Mittente del messaggio da visualizzare, NON il sender di GUIAcknwledgeMsg
     * @return
     */
    public ActorRef getSender() {
        return sender;
    }
}
