package exercize01.model.messages;

import exercize01.model.Matrix;

public class StartMsg {
    private final int numGenerations;
    private final Matrix matrix;

    public StartMsg(final Matrix m) {
        this.numGenerations = 0;
        this.matrix = m;
        this.matrix.generateRandomMatrix();
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public Matrix getMatrix() {
        return matrix;
    }
}
