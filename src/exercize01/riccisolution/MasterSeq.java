package exercize01.riccisolution;

/**
 * This master does the job sequentially.
 * 
 * @author aricci
 *
 */
public class MasterSeq extends Thread {

	private View view;
	private CellGrid grid;
	private Flag stopFlag;
	private long period;
	
	public MasterSeq(CellGrid grid, View view, Flag stopFlag, int frameRate){
		this.grid = grid;
		this.view = view;
		this.stopFlag = stopFlag;
		period = 1000/frameRate;
	}
	
	public void run(){
		try {
			while (!stopFlag.isSet()){
				long current = System.currentTimeMillis();
				long currentNano = System.nanoTime();
				int nAliveCells = 0;
								
				for (int i = 0; i < grid.getWidth(); i++){
					for (int j = 0; j < grid.getHeight(); j++){
						boolean alive = grid.computeNextCellState(i, j);
						if (alive){
							nAliveCells++;
						}
					}
				}
				grid.swap();
				long dt = (System.nanoTime() - currentNano)/1000;
				view.update(nAliveCells, dt);
				waitForNextFrame(current);
			}			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Take a little nap to synch with the frame rate
	 * 
	 * @param current
	 */
	protected void waitForNextFrame(long current){
		long dt = System.currentTimeMillis() - current;
		if (dt < period){
			try {
				Thread.sleep(period-dt);
			} catch (Exception ex){}
		}
	}

	private void log(String msg){
		synchronized(System.out){
			System.out.println(msg);
		}
	}
	
}
