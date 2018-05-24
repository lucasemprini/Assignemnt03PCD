package exercize01.ass01;

public class CellCountLatch {

	private int nParticipants;
	private int nCells;
	private int nCounts;
	
	public CellCountLatch(int nParticipants) {
		this.nParticipants = nParticipants;
		nCounts = 0;
		nCells = 0;
	}
	
	public synchronized void countDown(int cell) {
		nCounts++;
		nCells += cell;
		notifyAll();
	}
	
	public synchronized int await() throws InterruptedException  {
		while (nCounts < nParticipants) {
			wait();
		}
		nCounts = 0;
		return nCells;
	}
}
