package exercize02.model.messages;

import akka.actor.ActorRef;

import java.util.List;

public class OtherActors {

    private final List<ActorRef> actors;

    public OtherActors(final List<ActorRef> actor) {
        this.actors = actor;
    }

    public List<ActorRef> getActors() {
        return actors;
    }
}
