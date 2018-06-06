package exercize02.model.messages;

/**
 * Richiesta di visualizzazione a video di un messaggio.
 */
public class ShowMsg {
    private final String msg;

    public ShowMsg(final String msg) {
        this.msg = msg;
    }

    /**
     * Messaggio da visualizzare.
     * @return
     */
    public String getMsg() {
        return msg;
    }
}
