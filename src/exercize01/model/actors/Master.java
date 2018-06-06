package exercize01.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import exercize01.Main;
import exercize01.model.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Master extends AbstractActor {
    private static final int FIXED_START_COLUMN = 0;
    public static int WORKER_MULTIPLIER = 1;
    public static int NUM_WORKERS = Runtime.getRuntime().availableProcessors() * WORKER_MULTIPLIER;
    private static boolean DEBUG = true;
    private static int counter = 0;
    private int numGeneration = 1;
    private long current;
    private boolean running = false;
    private List<ActorRef> workers = new ArrayList<>();

    /**
     * Messaggi a cui risponde:
     * -StartSystemMsg:
     * Se non ha già dato l'avvio ai worker li richiama e li avvia.
     * Nel caso in cui sia il primo avvio crea anche i worker
     * -CallWorkerMsg:
     * Se non si è in stato di stop e non sono stati lanciati tutti i worker li richiama e ne avvia uno.
     * Eseguita questa operazione si richiama se devono essere chiamati altri worker.
     * -UpdateGUIMsg:
     * Lavora ideologicamente come una CyclicBarrier, dopo essere stato contatto tot volte significa che i worker hanno
     * terminato i rispettivi compiti sulla matrice ed è pronta per essere renderizzata.
     * -StopMsg:
     * Notifica che dalla GUI è stato dato input di bloccare le operazioni.
     * Propaga il messaggio anche a tutti i worker
     *
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartSystemMsg.class, startSystem -> {
            System.out.println("Starting a GameOfLife with a "
                    + Main.GAME_MATRIX.getNumRows() +"x"
                    + Main.GAME_MATRIX.getNumRows()
                    + " Matrix and " + NUM_WORKERS + " workers:\n");
            if (!isRunning()) {
                setRunning(true);
                current = System.currentTimeMillis();

                if (workers.isEmpty()) {
                    IntStream.range(1, NUM_WORKERS)
                            .forEach(i -> workers.add(getContext().getSystem().actorOf(Props.create(Worker.class))));
                }
                getSelf().tell(new CallWorkerMsg(0, 1), ActorRef.noSender());
            }
        }).match(CallWorkerMsg.class, callWorker -> {
            if (isRunning()) {
                int wOffset = callWorker.getRow() + getOffset();
                if (wOffset >= Main.GAME_MATRIX.getNumRows()) wOffset = Main.GAME_MATRIX.getNumRows() - 1;

                getContext().actorOf(Props.create(Worker.class)).tell(new StartMsg(Main.GAME_MATRIX,
                                callWorker.getRow(), wOffset,
                                FIXED_START_COLUMN, Main.GAME_MATRIX.getNumColumns() - 1, numGeneration),
                        getSelf());


                if (callWorker.getWorker() < NUM_WORKERS) {
                    getSelf().tell(new CallWorkerMsg(wOffset + 1,
                                    callWorker.getWorker() + 1),
                            ActorRef.noSender());
                }
            }
        }).match(UpdateGUIMsg.class, e -> {
            counter++;
            if (counter == NUM_WORKERS) {
                Main.GAME_MATRIX.computeUpdate();
                Main.VIEW.update(Main.GAME_MATRIX.getAliveCellsAndReset(), System.currentTimeMillis() - current);
                numGeneration++;
                reset();

                if (isRunning()) {
                    getSelf().tell(new CallWorkerMsg(0, 1), ActorRef.noSender());
                }

            }
        }).match(StopMsg.class, msg -> {
            if (!isRunning()) return;
            setRunning(false);
            if (DEBUG) {
                System.out.println("Stop Received!");
            }
            workers.forEach(w -> w.tell(new StopMsg(), getSelf()));
        }).build();
    }


    private void reset() {
        counter = 0;
        current = System.currentTimeMillis();
    }

    private int getOffset() {
        return Main.GAME_MATRIX.getNumRows() / NUM_WORKERS - 1;
    }


    public boolean isRunning() {
        return running;
    }

    private void setRunning(final boolean state) {
        this.running = state;
    }
}
