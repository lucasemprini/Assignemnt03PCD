package exercize01.model.messages;

import exercize01.model.Matrix;

public class ComputeUpdateMsg {
    private final Matrix m;
    private final int numGeneration;

    public ComputeUpdateMsg(final Matrix m, final int generation){
        this.m = m;
        this.numGeneration = generation;
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
}
