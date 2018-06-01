package exercize01.model.messages;

import exercize01.model.Matrix;

public class ComputeUpdateMatrixMsg {
    private final Matrix m;
    private final int numGeneration;
    private final int startRow;
    private final int stopRow;
    private final int startColumn;
    private final int stopColumn;



    public ComputeUpdateMatrixMsg(final Matrix m, final int generation, final int startRow,
                                  final int stopRow, final int startColumn, final int stopColumn){
        this.m = m;
        this.numGeneration = generation;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.stopRow = stopRow;
        this.stopColumn = stopColumn;
    }

    public void computeUpdate(){
        this.m.computeUpdate();
    }

    public Matrix getMatrix() {
        return this.m;
    }

    public int getNumGeneration() {
        return this.numGeneration;
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
