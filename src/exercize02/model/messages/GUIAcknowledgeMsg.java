package exercize02.model.messages;

import akka.actor.ActorRef;

public class GUIAcknowledgeMsg {
    private final String msg;
    private final ActorRef sender;

    public GUIAcknowledgeMsg(final String msg, final ActorRef sender) {
        this.msg = msg;
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public ActorRef getSender() {
        return sender;
    }
}
