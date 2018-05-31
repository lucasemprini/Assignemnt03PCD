package exercize01.controller;

import exercize01.model.Flag;
import exercize01.model.InputListener;
import exercize01.model.Matrix;
import exercize01.view.View;

public class Controller implements InputListener {

	private Matrix matrix;
	private Flag stopFlag;
	private View view;

	public Controller(Matrix matrix, View view){
		this.matrix = matrix;
		this.view = view;
	}
	
	public void started(){
		stopFlag = new Flag();
	}

	public void stopped() {
		stopFlag.set();
	}

    public Matrix getMatrix() {
        return matrix;
    }

    public View getView() {
        return view;
    }
}
