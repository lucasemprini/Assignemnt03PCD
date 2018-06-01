package exercize01.model.actors;

import akka.actor.AbstractActor;
import exercize01.model.Matrix;
import exercize01.model.messages.StartMsg;
import exercize01.model.messages.StartSystem;
import exercize01.model.messages.UpdateGUIMsg;
import exercize01.view.View;

public class Master extends AbstractActor {
    private View view;
    private Matrix matrix;
    private long current;
    private int counter = 0;
    private static final int WORKER_MULTIPLIER = 1;

    public static int getWorkers() {
        return (Runtime.getRuntime().availableProcessors() + 1) * WORKER_MULTIPLIER;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartSystem.class, startSystem -> {
            current = System.currentTimeMillis();

            //TODO suddividere i compiti tra i vari workers
        }).match(UpdateGUIMsg.class, e -> {
            counter++;
            if (counter == Master.getWorkers()) {
                view.update(matrix.getAliveCells(), current - System.currentTimeMillis());
                reset();
            }
        }).build();
    }

    private void reset() {
        counter = 0;
    }
}
