package exercize01.model.messages;

import exercize01.model.Matrix;

public class UpdateMatrixMsg {
    private final Matrix m;
    private final int numGenerations;
    private final int startRow;
    private final int stopRow;
    private final int startColumn;
    private final int stopColumn;

    public UpdateMatrixMsg(final Matrix m, final int generation, final int startRow,
                            final int stopRow, final int startColumn, final int stopColumn){
        this.m = m;
        this.numGenerations = generation;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.stopRow = stopRow;
        this.stopColumn = stopColumn;
    }

    public void update(){
        this.m.update(startRow, stopRow, startColumn, stopColumn);
    }

    public Matrix getMatrix() {
        return this.m;
    }

    public int getNumGenerations() {
        return this.numGenerations;
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
