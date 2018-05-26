package exercize01.riccisolution;

/**
 * Compute the next state of sub-part of the grid
 * 
 */
class ComputeGridStateWorker extends Thread {
	
	private int x0,y0,w,h;
	private CellGrid grid;
	private CellCountLatch done;
	private FrameSynch canGo;
	private Flag stopFlag;
	
	public ComputeGridStateWorker(CellGrid grid,  int x0, int y0, int w, int h, CellCountLatch done, FrameSynch canGo, Flag stopFlag){
		this.x0 = x0;
		this.y0 = y0;
		this.w = w;
		this.h = h;
		this.grid = grid;
		this.done = done;
		this.stopFlag = stopFlag;
		this.canGo = canGo;
	}
	
	public void run(){
		while (!stopFlag.isSet()) {
			int nAliveCells = 0;
			for (int i = 0; i < w; i++){
				for (int j = 0; j < h; j++){
					boolean alive = grid.computeNextCellState(x0+i,y0+j);
					if (alive){
						nAliveCells++;
					}
				}
			}
			/* notify completion */
			done.countDown(nAliveCells);
			try {
				/* wait for next frame to compute */
				canGo.awaitToGo();
			} catch (Exception ex) {
			}
		}
	}
}
