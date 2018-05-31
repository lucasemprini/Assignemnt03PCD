package exercize01.model.messages;

import exercize01.model.Matrix;

public class StartMsg {
    private final int gen;
    private final Matrix matrix;

    public StartMsg(final Matrix m, final int gen) {
        this.gen = 0;
        this.matrix = m;
        this.matrix.generateRandomMatrix();
    }

    public int getGen() {
        return gen;
    }

    public Matrix getMatrix() {
        return matrix;
    }
}
