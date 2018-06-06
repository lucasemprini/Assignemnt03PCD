package exercize02.model.actors;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import exercize02.Main;
import exercize02.model.messages.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class User extends AbstractActorWithStash {

    /**
     * Tempo minimo per cui l'user deve tenere il token.
     */
    private static final int TOKEN_TIME = 50;

    private final String name;
    private final Queue<String> buffer = new LinkedList<>();
    private ActorRef registry;
    private ActorRef guiActor;
    private List<ActorRef> otherActors;
    private long startTime;
    private boolean token = false;
    private int nWaiting = 0;
    private int counter = 0;
    private boolean wantExit = false;

    public User(final String name) {
        this.name = name;
    }

    /**
     * Funzionalità per ogni messaggio:
     * - StartUser
     * l'attore lo gestisce solo la prima volta, utilizzato per ottenere il riferimento al Register e al GUI Actor.
     * - SendMsg
     * Riceve l'intenzione da parte dell'attore di inviare un messaggio e lo appoggia nel buffer.
     * Verrà gestito alla ricezione del token.
     * - RemActorButtonPressedMsg
     * riceve che l'utente è intenzionato ad uscire e viene salvato.
     * Verrà poi gestita l'uscita solo quando si otterrà il token e tutti i messaggi nel buffer saranno inviati.
     * - TakeToken
     * riceve il token ed avvia tutte le operazioni, in ordine:
     * 1 Invio dei messaggi presenti nel Buffer in modalità FIFO
     * 2 Avvio del procedimento di rilascio del token
     * 3 Controllo della volontà di uscire, se SI viene gestita
     * 4 rilascio effettivo del token
     * - OtherActors
     * viene ricevuto questo messaggio dal Registry dopo avergli chiesto gli altri attori registrati.
     * Una volta ottenuti avvia la procedura di invio dei messaggi
     * - SendBroadcastMsg
     * verifica che il messaggio da inviare esista, se SI:
     * 1 salva il numero di attori da cui dovrà ricevere l'acknowledge, compreso se stesso, così che funzioni anche se esiste un solo attore.
     * 2 manda a tutti gli altri attori uno ShowMsg con il messaggio da inviare
     * 3 notifica a se stesso che il messaggi sono stati inviati con un AcknowledgeMsg
     * se NO:
     * manda un messaggio a se stesso dicendo di avviare le operazione pre-passaggio token
     * - AcknowledgeMsg
     * incrementa il contatore delle ricevute, se è maggiore o uguale al numero di attori che si aspettavano un messaggio
     * significa che tutti hanno ricevuto il messaggio e visualizzato, perciò dice al GuiActor che anche se stesso può visualizzarlo e resetta tutte le variabili.
     * - GUIAcknowledgeMsg
     * ricevuta di ritorno da parte della GUI, indica che il messaggio è stato renderizzato.
     * Esegue a questo punto il controllo se chi ha inviato il messaggio sia lo stesso attore che lo gestisce, se SI:
     * Significa che sono l'attore con il token perciò tento di mandare un altro messaggio presente sul buffer
     * se NO:
     * significa che è stato ricevuto un msg da parte di chi ha il token e viene notificato al mittente che il messaggio
     * è stato visualizzato
     * - ShowMsg
     * ricevuto quando si richiede di visualizzare un messaggio,
     * viene girato al guiActor che lo gestirà e ne notificherà l'esito
     * - TerminateUserOperation
     * verifica se c'è la volontà di uscire, se SI:
     * chiede al registro di eliminarlo dalla lista di attori e aspetta una sua conferma, in CanExit
     * se NO:
     * avvia le procedure di passaggio del token, mandando un messaggio al registry.
     * - CanExit
     * permesso da parte del registry di uscire dalla chat
     *
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartUser.class, startUser -> {
            if (registry == null && guiActor == null) {
                registry = startUser.getRegistry();
                guiActor = startUser.getGuiActor();
            }
        }).match(SendMsg.class, msg -> {
            stashOrDo(() -> buffer.add(msg.getMessage()));

        }).match(RemActorButtonPressedMsg.class, remActorButtonPressedMsg -> {
            stashOrDo(() -> wantExit = true);

        }).match(TakeToken.class, takeToken -> {
            takeToken();
            registry.tell(new GetMeOthers(), getSelf());

        }).match(OtherActors.class, otherActors -> {
            this.otherActors = otherActors.getActors();
            getSelf().tell(new SendBroadcastMsg(buffer.poll(), this.otherActors), ActorRef.noSender());

        }).match(SendBroadcastMsg.class, sendBroadcastMsg -> {
            if (sendBroadcastMsg.getMsg() != null) {
                setWaitingActors(sendBroadcastMsg.getActors().size() + 1);
                sendBroadcastMsg.getActors().forEach(actorRef -> actorRef.tell(new ShowMsg(sendBroadcastMsg.getMsg()), getSelf()));
                getSelf().tell(new AcknowledgeMsg(sendBroadcastMsg.getMsg()), getSelf());
            } else {
                getSelf().tell(new TerminateUserOperation(), ActorRef.noSender());
            }

        }).match(AcknowledgeMsg.class, acknowledgeMsg -> {
            incCounter();
            if (isWaiting()) {
                guiActor.tell(new GUIShowMsg("Me: ", acknowledgeMsg.getMsg(), getSelf()), getSelf());
                reset();
            }

        }).match(GUIAcknowledgeMsg.class, guiAcknowledgeMsg -> {
            if (guiAcknowledgeMsg.getSender() == getSelf()) {
                getSelf().tell(new SendBroadcastMsg(buffer.poll(), otherActors), ActorRef.noSender());
            } else {
                guiAcknowledgeMsg.getSender().tell(new AcknowledgeMsg(guiAcknowledgeMsg.getMsg()), getSelf());
            }

        }).match(ShowMsg.class, showMsg -> {
            guiActor.tell(new GUIShowMsg(getSender().path().name() + ": ", showMsg.getMsg(), getSender()), getSelf());

        }).match(TerminateUserOperation.class, terminateUserOperation -> {
            if (wantExit) {
                registry.tell(new RemActorButtonPressedMsg(), getSelf());
                wantExit = false;
            } else {
                passToken();
            }

        }).match(CanExit.class, canExit -> {
            guiActor.tell(new CanExit(getSelf()), ActorRef.noSender());
            passToken();

        }).build();
    }

    /**
     * Restituisce il nome dell'attore.
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return TRUE se si ha il token, FALSE altrimenti.
     */
    public boolean hasToken() {
        return token;
    }

    /**
     * Operazioni da esegui alla presa del token.
     */
    public void takeToken() {
        this.token = true;
        startTime = System.currentTimeMillis();
        unstashAll();
    }

    /**
     * Controlla che il tempo minimo di mantenimento del token sia rispettatto.
     * Dopo di che manda un messaggio al registry dicendo che può avviare la procedura di scambio.
     */
    private void passToken() {
        long sleep = TOKEN_TIME - (System.currentTimeMillis() - startTime);
        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (Exception ex) {
                log(ex.getMessage());
            }
        }
        registry.tell(new PassToken(), getSelf());
        this.token = false;
    }

    /**
     * @return TRUE in attesa, FALSE altrimenti
     */
    private boolean isWaiting() {
        return getCounter() >= getWaitingActors();
    }


    /**
     * Numero di attori da cui ci si aspetta una notifica di ricezione del messaggio.
     *
     * @return
     */
    private int getWaitingActors() {
        return nWaiting;
    }

    /**
     * Imposta il numero di attori da cui ci si aspetta una notifica di ricezione del messaggio.
     *
     * @param nWaiting numero di attori, per funzionare bene deve essere compreso di se stesso.
     */
    private void setWaitingActors(int nWaiting) {
        this.nWaiting = nWaiting;
    }

    /**
     * Incrementa il contatore
     */
    private void incCounter() {
        counter++;
    }

    /**
     * @return Numero di ricevute di ritorno rilevate
     */
    private int getCounter() {
        return counter;
    }

    /**
     * Resetta lo stato di invio del messaggio.
     */
    private void reset() {
        counter = 0;
        setWaitingActors(0);
    }

    /**
     * Verifica la presenza del token, se non c'è esegue la stash(), altrimenti esegue l'operazione fornita come parametro.
     *
     * @param operation
     */
    private void stashOrDo(final Operation operation) {
        if (hasToken()) {
            operation.compute();
        } else {
            stash();
        }
    }

    private void log(final String msg) {
        if (Main.DEBUG) System.out.println("User " + getName() + " : " + msg);
    }


}
