package exercize01.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize01.Main;
import exercize01.model.Matrix;
import exercize01.model.messages.*;
import exercize01.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Master extends AbstractActor {
    private static final int WORKER_MULTIPLIER = 1;
    private static final int FIXED_START_COLUMN = 0;
    private static boolean DEBUG = true;
    private int numGeneration = 1;
    private long current;
    private static int counter = 0;
    private boolean stop = false;
    private List<ActorRef> workers = new ArrayList<>();

    public static int getWorkers() {
        return 10;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartSystem.class, startSystem -> {
            stop = false;
            current = System.currentTimeMillis();

            IntStream.range(0, getWorkers()).forEach(i -> workers.add(getContext().getSystem().actorOf(Props.create(StoppableActor.class))));
            getSelf().tell(new CallWorker(0, 1), ActorRef.noSender());
        }).match(CallWorker.class, callWorker -> {
            if (!isStopped()) {
                int wOffset = callWorker.getRow() + getOffset();
                if (callWorker.getWorker() <= getWorkers()) {


                    if (wOffset >= Main.GAME_MATRIX.getNumRows()) wOffset = Main.GAME_MATRIX.getNumRows() - 1;
                    System.out.println("From: " + callWorker.getRow() + " to: " + wOffset);

                    getContext().actorOf(Props.create(StoppableActor.class)).tell(new StartMsg(Main.GAME_MATRIX,
                                    callWorker.getRow(), wOffset,
                                    FIXED_START_COLUMN, Main.GAME_MATRIX.getNumColumns(), numGeneration),
                            getSelf());
                }


                if (callWorker.getWorker() < getWorkers()) {
                    getSelf().tell(new CallWorker(wOffset + 1,
                                    callWorker.getWorker() + 1),
                            ActorRef.noSender());
                }
            }
        }).match(UpdateGUIMsg.class, e -> {
            counter++;
            if (counter == Master.getWorkers()) {
                System.out.println("UpdateGUIMsg");
                Main.VIEW.update(Main.GAME_MATRIX.getAliveCells(),  System.currentTimeMillis() - current);
                numGeneration++;
                reset();

                if (!isStopped()) {
                    getSelf().tell(new CallWorker(0,  1), ActorRef.noSender());
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
        return Main.GAME_MATRIX.getNumRows() / getWorkers() - 1;
    }


}
