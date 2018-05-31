package exercize01.model.messages;

import exercize01.model.Matrix;

public class ComputeUpdateMsg {
    private final Matrix m;
    private final int generation;

    public ComputeUpdateMsg(final Matrix m, final int generation){
        this.m = m;
        this.generation = generation;
    }

    public void computeUpdate(){
        this.m.computeUpdate();
    }

    public Matrix getMatrix() {
        return this.m;
    }

    public int getGeneration() {
        return this.generation;
    }
}
