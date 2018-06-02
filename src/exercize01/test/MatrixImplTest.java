package exercize01.test;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize01.controller.Controller;
import exercize01.model.Matrix;
import exercize01.model.MatrixImpl;
import exercize01.model.actors.Worker;
import exercize01.model.messages.StartMsg;
import exercize01.model.messages.StopMsg;
import exercize01.model.utility.Chrono;
import exercize01.model.utility.DebugUtility;
import org.junit.Test;

public class MatrixImplTest {

    private final Chrono chronometer = new Chrono();
    private static final int NUM_ROWS = 5000;
    private static final int NUM_COLUMNS = 5000;
    private static final int GENERATIONS = 10;
    private static final String TIME_STRING = "Time elapsed (in millis): ";

    private final Matrix testMatrix = new MatrixImpl(NUM_ROWS, NUM_COLUMNS);
    private final Controller controller = new Controller(testMatrix);
    @Test
    public void executeTenGenerations() {
        long totalTime = 0;
        this.testMatrix.generateRandomMatrix();
        this.chronometer.start();
        for(int i = 0; i < GENERATIONS; i++) {
            this.testMatrix.update(0, NUM_ROWS, 0, NUM_COLUMNS);
            this.testMatrix.computeUpdate();
            totalTime += this.chronometer.getTime();
            this.chronometer.stop();
            DebugUtility.printOnlyGeneration(i + 1, this.chronometer.getTime(), this.testMatrix.getAliveCellsAndReset());

        }
        this.chronometer.stop();

        System.out.println(TIME_STRING + totalTime);
    }

    @Test
    public void executeWithActorsDummy() {
        this.chronometer.start();
        final ActorSystem system = ActorSystem.create("MySystem");
        final ActorRef act = system.actorOf(Props.create(Worker.class));
        act.tell(new StartMsg(this.testMatrix,
                0, this.testMatrix.getNumRows(),
                0, this.testMatrix.getNumColumns(), 0),
                ActorRef.noSender());


        act.tell(new StopMsg(), ActorRef.noSender());
        System.out.println("!! stop sent !!");
        this.chronometer.stop();
        System.out.println(TIME_STRING + this.chronometer.getTime());
    }

    public void executeWithController() {

    }
}