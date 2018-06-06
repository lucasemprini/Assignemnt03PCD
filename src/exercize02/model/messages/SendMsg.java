package exercize02.model.messages;

import javafx.collections.ObservableList;

/**
 * Richiesta ad un attore di inviare un messaggio.
 */
public class SendMsg {

    private final String whatMessage;
    private final ObservableList<String> listOfMessages;

    public SendMsg(final String whatMessage,
                   final ObservableList<String> listOfMessages) {
        this.whatMessage = whatMessage;
        this.listOfMessages = listOfMessages;
    }

    /**
     * Messaggio da inviare
     * @return
     */
    public String getMessage() {
        return this.whatMessage;
    }

    /**
     * Non usato.
     * @return
     */
    public ObservableList<String> getListOfMessages() {
        return this.listOfMessages;
    }
}
