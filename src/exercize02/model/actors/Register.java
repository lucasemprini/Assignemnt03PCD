package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.typesafe.config.ConfigFactory;
import exercize02.model.messages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Register extends AbstractActor {
    private final List<ActorRef> registry = new ArrayList<>();
    private int position = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ActorsInRegistryAskMsg.class, msg -> {
            //TODO cosa fare quando si chiede la lista degli attori nel Registro.
        }).match(AddActorButtonPressedMsg.class, msg -> {
            try {
                registry.add(getSender());
            } catch (Exception e) {
                System.out.println("Impossibile registrare l'attore.\n" + e.getMessage());
            }
        }).match(RemActorButtonPressedMsg.class, msg -> {
            try {
                registry.remove(getSender());
                getSender().tell(new CanExit(), getSender());
            } catch (Exception e) {
                System.out.println("Impossibile registrare l'attore.\n" + e.getMessage());
            }
        }).match(GetMeOthers.class, getMeOthers -> {
            try {
                List<ActorRef> app = new LinkedList<>();
                Collections.copy(app, registry);

                app.remove(getSender());

                getSender().tell(new OtherActors(app), getSelf());
            } catch (Exception ex) {
                System.out.println("Rispondere alla getMeOthers.\n" + ex.getMessage());
            }
        }).build();
    }
}
