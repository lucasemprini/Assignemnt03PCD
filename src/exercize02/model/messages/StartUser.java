package exercize02.model.messages;

import akka.actor.ActorRef;

public class StartUser {

    private final ActorRef registry;
    private final ActorRef guiActor;

    public StartUser(ActorRef registry, ActorRef guiActor) {
        this.registry = registry;
        this.guiActor = guiActor;
    }

    public ActorRef getRegistry() {
        return registry;
    }

    public ActorRef getGuiActor() {
        return guiActor;
    }
}
