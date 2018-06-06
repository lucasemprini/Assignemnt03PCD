package exercize02.model.messages;

/**
 * Messaggio di ricezione del token.
 */
public class TakeToken {

    private final int position;

    public TakeToken(int position) {
        this.position = position;
    }

    /**
     * Posizione dell'attore all'interno della lista
     * @return
     */
    public int getPosition() {
        return position;
    }
}
