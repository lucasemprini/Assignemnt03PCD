package exercize01.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import exercize01.Main;
import exercize01.model.messages.*;
import exercize01.model.utility.Chrono;
import exercize01.model.utility.DebugUtility;

public class StoppableActor extends AbstractActor {
	private boolean stopped;
	private final Chrono chrono = new Chrono();
	private static boolean DEBUG = false;
    private ActorRef master;

	private void stopMe() {
	    this.stopped = true;
    }

    private void startMe() {
	    this.stopped = false;
    }

	public Receive createReceive() {
		return receiveBuilder().match(StartMsg.class, msg -> {
			startMe();
			this.chrono.start();
			master = getSender();
			getSelf().tell(new UpdateMatrixMsg(Main.GAME_MATRIX,
                    msg.getNumGenerations(), msg.getStartRow(),
                    msg.getStopRow(), msg.getStartColumn(),
                    msg.getStopColumn()), ActorRef.noSender());
		}).match(UpdateMatrixMsg.class, msg -> {
            if (!stopped) {
                msg.update();
                getSelf().tell(new ComputeUpdateMatrixMsg(msg.getMatrix(),
                        msg.getNumGenerations(), msg.getStartRow(),
                        msg.getStopRow(), msg.getStartColumn(),
                        msg.getStopColumn()), ActorRef.noSender());
            }
		}).match(ComputeUpdateMatrixMsg.class, msg -> {
			if (!stopped){

                this.chrono.stop();

				master.tell(new UpdateGUIMsg(),
						ActorRef.noSender());
                if(DEBUG) {
                    DebugUtility.printOnlyGeneration(msg.getNumGeneration() + 1,
                            this.chrono.getTime(), msg.getMatrix().getAliveCellsAndReset());
                }



			}
		}).match(StopMsg.class, msg -> {
		    System.out.println("Stoppable is stopped");
		    stopMe();
		}).build();
	}

	public void setDebug(final boolean debug) {
	    DEBUG = debug;
    }
}
