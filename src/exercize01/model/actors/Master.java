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
    public static int WORKER_MULTIPLIER = 1;
    public static int NUM_WORKERS = Runtime.getRuntime().availableProcessors() * WORKER_MULTIPLIER;
    private static final int FIXED_START_COLUMN = 0;
    private static boolean DEBUG = true;
    private int numGeneration = 1;
    private long current;
    private static int counter = 0;
    private boolean stop = false;
    private List<ActorRef> workers = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartSystemMsg.class, startSystem -> {
            stop = false;
            current = System.currentTimeMillis();

            IntStream.range(0, NUM_WORKERS).forEach(i ->
                    workers.add(getContext().getSystem().actorOf(Props.create(Worker.class))));
            getSelf().tell(new CallWorkerMsg(0, 1), ActorRef.noSender());
        }).match(CallWorkerMsg.class, callWorker -> {
            if (!isStopped()) {
                int wOffset = callWorker.getRow() + getOffset();
                if (callWorker.getWorker() <= NUM_WORKERS) {


                    if (wOffset >= Main.GAME_MATRIX.getNumRows()) wOffset = Main.GAME_MATRIX.getNumRows() - 1;

                    getContext().actorOf(Props.create(Worker.class)).tell(new StartMsg(Main.GAME_MATRIX,
                                    callWorker.getRow(), wOffset,
                                    FIXED_START_COLUMN, Main.GAME_MATRIX.getNumColumns() - 1, numGeneration),
                            getSelf());
                }


                if (callWorker.getWorker() < NUM_WORKERS) {
                    getSelf().tell(new CallWorkerMsg(wOffset + 1,
                                    callWorker.getWorker() + 1),
                            ActorRef.noSender());
                }
            }
        }).match(UpdateGUIMsg.class, e -> {
            counter++;
            if (counter == Master.NUM_WORKERS) {
                System.out.println("UpdateGUIMsg");
                Main.GAME_MATRIX.computeUpdate();
                Main.VIEW.update(Main.GAME_MATRIX.getAliveCellsAndReset(),  System.currentTimeMillis() - current);
                numGeneration++;
                reset();

                if (!isStopped()) {
                    getSelf().tell(new CallWorkerMsg(0,  1), ActorRef.noSender());
                }

            }
        }).match(StopMsg.class, msg -> {
            if (isStopped()) return;
            stop = true;
            if (DEBUG) {
                System.out.println("stopped!");
            }
            workers.forEach(w -> w.tell(new StopMsg(), getSelf()));
        }).build();
    }

    private boolean isStopped() {
        return stop;
    }

    private void reset() {
        counter = 0;
        current = System.currentTimeMillis();
    }

    private int getOffset() {
        return Main.GAME_MATRIX.getNumRows() / NUM_WORKERS - 1;
    }


}
