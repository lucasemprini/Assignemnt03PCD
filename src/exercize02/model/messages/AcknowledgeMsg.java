package exercize02.model.messages;

public class AcknowledgeMsg {
    private final String msg;

    public AcknowledgeMsg(final String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
