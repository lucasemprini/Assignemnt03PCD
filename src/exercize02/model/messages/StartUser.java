package exercize02.model.messages;

import akka.actor.ActorRef;

/**
 * Setting di un nuovo attore nella chat.
 */
public class StartUser {

    private final ActorRef registry;
    private final ActorRef guiActor;

    public StartUser(ActorRef registry, ActorRef guiActor) {
        this.registry = registry;
        this.guiActor = guiActor;
    }

    /**
     * Registro.
     * @return
     */
    public ActorRef getRegistry() {
        return registry;
    }

    /**
     * Attore gestore della GUI.
     * @return
     */
    public ActorRef getGuiActor() {
        return guiActor;
    }
}
