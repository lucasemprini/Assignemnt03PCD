package exercize01.model.messages;

import exercize01.model.Matrix;

public class UpdateMsg {
    private final Matrix m;
    private final int numGenerations;

    public UpdateMsg(final Matrix m, final int generation){
        this.m = m;
        this.numGenerations = generation;
    }

    public void update(){
        this.m.update(0, m.getNumRows(), 0, m.getNumColumns());
    }

    public Matrix getMatrix() {
        return this.m;
    }

    public int getNumGenerations() {
        return this.numGenerations;
    }
}
