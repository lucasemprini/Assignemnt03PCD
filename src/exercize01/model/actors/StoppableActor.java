package exercize01.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import exercize01.model.messages.ComputeUpdateMsg;
import exercize01.model.messages.StartMsg;
import exercize01.model.messages.StopMsg;
import exercize01.model.messages.UpdateMsg;
import exercize01.model.utility.Chrono;
import exercize01.model.utility.DebugUtility;

public class StoppableActor extends AbstractActor {
	private boolean stopped;
	private final Chrono chrono = new Chrono();
	private static boolean DEBUG = true;

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
			getSelf().tell(new UpdateMsg(msg.getMatrix(), msg.getNumGenerations()), ActorRef.noSender());
		}).match(UpdateMsg.class, msg -> {
            if (!stopped) {
                msg.update();
                getSelf().tell(new ComputeUpdateMsg(msg.getMatrix(), msg.getNumGenerations()), ActorRef.noSender());
            }
		}).match(ComputeUpdateMsg.class, msg -> {
			if (!stopped){
				msg.computeUpdate();
                this.chrono.stop();

                if(DEBUG) {
                    DebugUtility.printOnlyGeneration(msg.getNumGeneration() + 1,
                            this.chrono.getTime(), msg.getMatrix().getAliveCells());
                }

				getSelf().tell(new UpdateMsg(msg.getMatrix(), msg.getNumGeneration() + 1), ActorRef.noSender());
			}
		}).match(StopMsg.class, msg -> {
		    if(DEBUG) {
		        System.out.println("stopped!");
		    }
		    stopMe();
		}).build();
	}

	public void setDebug(final boolean debug) {
	    DEBUG = debug;
    }
}
