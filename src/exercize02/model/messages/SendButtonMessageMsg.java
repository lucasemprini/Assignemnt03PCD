package exercize02.model.messages;

import akka.actor.ActorRef;
import javafx.collections.ObservableList;

/**
 * Richiesta di invio da parte dell'utente di un messaggio.
 */
public class SendButtonMessageMsg {

    private final String whatMessage;
    private final ActorRef whoSends;
    private final ObservableList<String> listOfMessages;

    public SendButtonMessageMsg(final ActorRef whoSends, final String whatMessage,
                                final ObservableList<String> listOfMessages) {
        this.whatMessage = whatMessage;
        this.whoSends = whoSends;
        this.listOfMessages = listOfMessages;
    }

    public String getMessage() {
        return this.whatMessage;
    }
    public ActorRef getSender() {
        return this.whoSends;
    }
    public ObservableList<String> getListOfMessages() {
        return this.listOfMessages;
    }
}
