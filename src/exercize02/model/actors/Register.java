package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import exercize02.Main;
import exercize02.model.messages.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Register extends AbstractActor {
    private final List<ActorRef> actors = new ArrayList<>();
    private int position = 0;


    /**
     * Funzionalità per ogni messaggio:
     *  - AddActorButtonPressedMsg
     *      aggiunge alla lista degli attori registrati il sender.
     *  - RemActorButtonPressedMsg
     *      rimuove dalla lista degli attori registrati il sender e gli notificata tramite il messaggio CanExit
     *      che può avviare le operazioni di uscita
     *  - GetMeOthers
     *      Risponde a questo messaggio inviando al sender un messaggio OthrerActors nel quale fornisce la lista di attori
     *      registrati escluso quello che la richiede
     *  - PassToken
     *      Assegna il token all'elemento successivo della lista, raggiunto l'ultimo riparte.
     *      Glielo notifica tramite il messaggio takeToken
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddActorButtonPressedMsg.class, msg -> {
            try {
                actors.add(getSender());
            } catch (Exception e) {
                log("Impossibile registrare l'attore.\n" + e.getMessage());
            }
        }).match(RemActorButtonPressedMsg.class, msg -> {
            try {
                actors.remove(msg.getToBeRemoved());
                getSender().tell(new CanExit(msg.getToBeRemoved()), getSender());
            } catch (Exception e) {
                log("Impossibile registrare l'attore.\n" + e.getMessage());
            }
        }).match(GetMeOthers.class, getMeOthers -> {
            try {
                List<ActorRef> app = new LinkedList<>(actors);
                app.remove(getSender());

                getSender().tell(new OtherActors(app), getSelf());
            } catch (Exception ex) {
                log("Impossibile rispondere alla getMeOthers.\n" + ex.getMessage());
            }
        }).match(PassToken.class, passToken -> {
            try {
                position++;
                //if (position >= actors.size()) position = 0;
                position = position % (actors.size() -1);

                actors.get(position).tell(new TakeToken(), getSelf());
            } catch (Exception ex) {
                log("Impossibile eseguire il passToken. \n" + ex.getMessage());
            }
        }).build();
    }


    private void log(final String msg) {
        if (Main.DEBUG) System.out.println("Registry: " + msg);
    }
}
