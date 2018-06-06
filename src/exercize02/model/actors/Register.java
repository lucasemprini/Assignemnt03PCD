package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import exercize02.Main;
import exercize02.model.messages.*;
import exercize02.model.utility.Operation;

import java.util.ArrayList;
import java.util.List;

public class Register extends AbstractActor {
    private final List<ActorRef> actors = new ArrayList<>();


    /**
     * Funzionalità per ogni messaggio:
     *  - AddActorButtonPressedMsg
     *      aggiunge alla lista degli attori registrati il sender.
     *  - RemActorButtonPressedMsg
     *      rimuove dalla lista degli attori registrati il sender e gli notificata tramite il messaggio CanExit
     *      che può avviare le operazioni di uscita
     *  - GetMeActors
     *      Risponde a questo messaggio inviando al sender un messaggio AllActors nel quale fornisce la lista di attori
     *      registrati

     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddActorButtonPressedMsg.class, msg -> {
            loggedOperation(() -> actors.add(getSender()), "Impossibile registrare l'attore.");

        }).match(RemActorButtonPressedMsg.class, msg -> {
            loggedOperation(() -> {
                actors.remove(getSender());
                getSender().tell(new CanExit(getSender()), getSender());
            },"Impossibile registrare l'attore.");

        }).match(GetMeActors.class, getMeActors -> {
            loggedOperation(() -> {
                getSender().tell(new AllActors(actors), getSelf());
            }, "Impossibile rispondere alla getMeActors.");

        }).build();
    }


    /**
     * Esegue l'operazione all'interno di un try-catch e se Main.DEBUG = TRUE ne logga l'eventuali eccezioni.
     * @param operation
     * @param prefix
     */
    private void loggedOperation(final Operation operation, final String prefix) {
        try {
            operation.compute();
        } catch (Exception ex) {
            log(prefix + "\nError details: " + ex.getMessage());
        }
    }


    private void log(final String msg) {
        if (Main.DEBUG) System.out.println("Registry: " + msg);
    }
}
