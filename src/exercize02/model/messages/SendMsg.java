package exercize02.model.messages;

import javafx.collections.ObservableList;

public class SendMsg {

    private final String whatMessage;
    private final ObservableList<String> listOfMessages;

    public SendMsg(final String whatMessage,
                   final ObservableList<String> listOfMessages) {
        this.whatMessage = whatMessage;
        this.listOfMessages = listOfMessages;
    }

    public String getMessage() {
        return this.whatMessage;
    }
    public ObservableList<String> getListOfMessages() {
        return this.listOfMessages;
    }
}
