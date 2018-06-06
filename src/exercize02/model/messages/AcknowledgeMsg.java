package exercize02.model.messages;

/**
 * Ricevuta di ritorno da parte di una attore al sender che
 * gli ha inviato un messaggio
 */
public class AcknowledgeMsg {
    private final String msg;

    public AcknowledgeMsg(final String msg) {
        this.msg = msg;
    }

    /**
     * Messaggio scambiato.
     * @return
     */
    public String getMsg() {
        return msg;
    }
}
