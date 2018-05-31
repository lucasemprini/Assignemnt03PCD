package exercize01.model.test;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize01.model.Matrix;
import exercize01.model.MatrixImpl;
import exercize01.model.actors.StoppableActor;
import exercize01.model.messages.StartMsg;
import exercize01.model.messages.StopMsg;
import exercize01.model.utility.Chrono;
import exercize01.model.utility.DebugUtility;
import org.junit.Test;

public class MatrixImplTest {

    private final Chrono chronometer = new Chrono();
    private static final int NUMROWS = 5000;
    private static final int NUMCOLUMNS = 5000;
    private static final int GENERATIONS = 10;
    private static final String TIME_STRING = "Time elapsed (in millis): ";

    private final Matrix testMatrix = new MatrixImpl(NUMROWS, NUMCOLUMNS);

    @Test
    public void executeTenGenerations() {
        long totalTime = 0;
        testMatrix.generateRandomMatrix();
        this.chronometer.start();
        for(int i = 0; i < GENERATIONS; i++) {
            testMatrix.update(0, NUMROWS, 0, NUMCOLUMNS);
            testMatrix.computeUpdate();
            totalTime += this.chronometer.getTime();
            this.chronometer.stop();
            DebugUtility.printOnlyGeneration(i + 1, this.chronometer.getTime(), this.testMatrix.getAliveCells());

        }
        this.chronometer.stop();

        System.out.println(TIME_STRING + totalTime);
    }

    @Test
    public void executeWithActors() {
        this.chronometer.start();
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef act = system.actorOf(Props.create(StoppableActor.class));
        act.tell(new StartMsg(testMatrix, 0), ActorRef.noSender());

        try {
            Thread.sleep(10000);
        } catch (Exception ignored){}

        act.tell(new StopMsg(), ActorRef.noSender());
        System.out.println("!! stop sent !!");
        this.chronometer.stop();
        System.out.println(TIME_STRING + this.chronometer.getTime());
    }
}