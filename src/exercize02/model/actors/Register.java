package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import exercize02.model.messages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Register extends AbstractActor {
    private final List<ActorRef> actors = new ArrayList<>();
    private int position = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ActorsInRegistryAskMsg.class, msg -> {
            //TODO cosa fare quando si chiede la lista degli attori nel Registro.
        }).match(AddActorButtonPressedMsg.class, msg -> {
            try {
                actors.add(getSender());
            } catch (Exception e) {
                System.out.println("Impossibile registrare l'attore.\n" + e.getMessage());
            }
        }).match(RemActorButtonPressedMsg.class, msg -> {
            try {
                actors.remove(getSender());
                getSender().tell(new CanExit(), getSender());
            } catch (Exception e) {
                System.out.println("Impossibile registrare l'attore.\n" + e.getMessage());
            }
        }).match(GetMeOthers.class, getMeOthers -> {
            try {
                List<ActorRef> app = new LinkedList<>(actors);
                app.remove(getSender());

                getSender().tell(new OtherActors(app), getSelf());
            } catch (Exception ex) {
                System.out.println("Rispondere alla getMeOthers.\n" + ex.getMessage());
            }
        }).match(PassToken.class, passToken -> {
            try {
                position++;
                if (position >= actors.size()) position = 0;

                actors.get(position).tell(new TakeToken(), getSelf());
            } catch (Exception ex) {
                System.out.println("Impossibile eseguire il passToken. \n" + ex.getMessage());
            }
        }).build();
    }
}
