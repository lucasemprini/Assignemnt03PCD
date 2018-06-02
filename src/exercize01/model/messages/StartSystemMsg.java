package exercize01.model.messages;

import exercize01.model.Flag;
import exercize01.model.Matrix;
import exercize01.view.View;

public class StartSystemMsg {
    private final Matrix matrix;
    private final View view;
    private final Flag stopFlag;


    public StartSystemMsg(final Matrix matrix, final View view, final Flag stopFlag) {
        this.matrix =  matrix;
        this.view = view;
        this.stopFlag = stopFlag;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public View getView() {
        return view;
    }

    public Flag getStopFlag() {
        return stopFlag;
    }
}
