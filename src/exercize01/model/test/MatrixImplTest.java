package exercize01.model.test;


import exercize01.model.Matrix;
import exercize01.model.MatrixImpl;
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
}