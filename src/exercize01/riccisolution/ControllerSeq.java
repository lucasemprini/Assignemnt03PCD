package exercize01.riccisolution;

public class ControllerSeq implements InputListener {

	private CellGrid grid;
	private Flag 	stopFlag;
	private View view;
	private int frameRate;
	
	public ControllerSeq(CellGrid grid, View view, int frameRate){
		this.grid = grid;
		this.view = view;
		this.frameRate = frameRate;
	}
	
	public void started(){
		stopFlag = new Flag();
		new MasterSeq(grid,view,stopFlag,frameRate).start();
	}

	public void stopped() {
		stopFlag.set();
	}

}
