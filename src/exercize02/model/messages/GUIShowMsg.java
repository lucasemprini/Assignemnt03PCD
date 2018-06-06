package exercize02.model.messages;

import akka.actor.ActorRef;

public class GUIShowMsg {
    private final String msg;
    private final ActorRef sender;
    private final String prefix;

    public GUIShowMsg(final String prefix, final String msg, final ActorRef sender) {
        this.prefix = prefix;
        this.msg = msg;
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public ActorRef getSender() {
        return sender;
    }

    private String getPrefix() {
        return prefix;
    }

    public String getFullMsg() {
        return getPrefix() + getMsg();
    }
}
