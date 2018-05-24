package exercize01.ass01;

public class Controller implements InputListener {

	private CellGrid grid;
	private Flag 	stopFlag;
	private View view;
	private int frameRate;
	
	public Controller(CellGrid grid, View view, int frameRate){
		this.grid = grid;
		this.view = view;
		this.frameRate = frameRate;
	}
	
	public void started(){
		stopFlag = new Flag();
		new Master(grid,view,frameRate,stopFlag).start();
	}

	public void stopped() {
		stopFlag.set();
	}

}
