package exercize02.model.messages;

public class SendMessageMsg {

    private final String whatMessage;

    public SendMessageMsg(final String whatMessage) {
        this.whatMessage = whatMessage;
    }

    public String getMessage() {
        return this.whatMessage;
    }
}
