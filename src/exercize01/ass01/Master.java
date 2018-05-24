package exercize01.ass01;

/**
 * This master uses an Executor to parallelize the job.
 * 
 * @author aricci
 *
 */
public class Master extends Thread {

	private View view;
	private CellGrid grid;
	private Flag stopFlag;
	private long period;
	private CellCountLatch done;
	
	public Master(CellGrid grid, View view, int frameRate, Flag stopFlag){
		this.grid = grid;
		this.view = view;
		this.stopFlag = stopFlag;
		period = 1000/frameRate;
	}
	
	public void run(){
		try {
			int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
			this.done = new CellCountLatch(nWorkers);
			FrameSynch canGo = new FrameSynch(nWorkers);
			
			int x0 = 0;
			int dx = grid.getWidth() / nWorkers;
			for (int i = 0; i < nWorkers - 1; i++){
				new ComputeGridStateWorker(grid,x0,0,dx,grid.getHeight(), done, canGo, stopFlag).start();
				x0 += dx;
			}
			new ComputeGridStateWorker(grid, x0, 0, grid.getWidth()-x0, grid.getHeight(), done, canGo, stopFlag).start();
			
			while (!stopFlag.isSet()){
				long current = System.currentTimeMillis();				
				long currentNano = System.nanoTime();				

				/* wait for all workers to complete their job */
				int nAliveCells = done.await();
				
				/* swap the new state */
				grid.swap();

				long dt = (System.nanoTime() - currentNano)/1000;
				
				/* update the view */
				view.update(nAliveCells, dt);
				
				/* synch with the frame rate */
				waitForNextFrame(current);

				/* notify next frame to be computed */
				canGo.goOn();
			}			
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

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
