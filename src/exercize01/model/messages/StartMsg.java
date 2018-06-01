package exercize01.model.messages;

import exercize01.model.Matrix;

public class StartMsg {
    private final int numGenerations;
    private final Matrix matrix;
    private final int startRow;
    private final int stopRow;
    private final int startColumn;
    private final int stopColumn;

    public StartMsg(final Matrix m, final int startRow,
                    final int stopRow, final int startColumn, final int stopColumn, final int numGenerations) {
        this.numGenerations = 0;
        this.matrix = m;
        this.matrix.generateRandomMatrix();
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.stopRow = stopRow;
        this.stopColumn = stopColumn;
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStopRow() {
        return stopRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStopColumn() {
        return stopColumn;
    }
}
