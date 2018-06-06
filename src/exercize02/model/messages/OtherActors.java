package exercize02.model.messages;

import akka.actor.ActorRef;

import java.util.List;

/**
 * Notifica da parte del registry per fornire gli attori presenti in chat,
 * escluso il destinatario.
 */
public class OtherActors {

    private final List<ActorRef> actors;

    public OtherActors(final List<ActorRef> actor) {
        this.actors = actor;
    }

    /**
     * Lista di attori in chat, destinatario escluso.
     * @return
     */
    public List<ActorRef> getActors() {
        return actors;
    }
}
