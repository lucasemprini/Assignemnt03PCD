package exercize01.ass01;

public class FrameSynch {

	private boolean canGo;
	private int nParticipants;
	private int count;
	
	public FrameSynch(int nParticipants) {
		canGo = false;
		this.nParticipants = nParticipants;
		count = 0;
	}
	
	public synchronized void goOn() {
		canGo = true;
		notifyAll();
	}
	
	public synchronized void awaitToGo() throws InterruptedException {
		while (!canGo) {
			wait();
		}
		count++;
		if (count == nParticipants) {
			count = 0;
			canGo = false;
		}
	}
}
