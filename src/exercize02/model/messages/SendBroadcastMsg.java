package exercize02.model.messages;

import akka.actor.ActorRef;

import java.util.List;

public class SendBroadcastMsg {

    private final String msg;
    private final List<ActorRef> actors;

    public SendBroadcastMsg(final String msg, final List<ActorRef> actorRefs) {
        this.msg = msg;
        this.actors = actorRefs;
    }

    public String getMsg() {
        return msg;
    }

    public List<ActorRef> getActors() {
        return actors;
    }
}
