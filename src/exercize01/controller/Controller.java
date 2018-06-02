package exercize01.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize01.model.Flag;
import exercize01.model.InputListener;
import exercize01.model.Matrix;
import exercize01.model.actors.Master;
import exercize01.model.messages.StartSystemMsg;
import exercize01.model.messages.StopMsg;
import exercize01.view.View;

public class Controller implements InputListener {

	private Matrix matrix;
	private Flag stopFlag;
	private View view;
    private ActorRef master;

	public Controller(Matrix matrix, View view){
		this.matrix = matrix;
		this.view = view;
		master = ActorSystem.create("MySystem").actorOf(Props.create(Master.class));
	}

	public Controller(Matrix matrix) {
        this.matrix = matrix;
        master = ActorSystem.create("MySystem").actorOf(Props.create(Master.class));
    }
	
	public void started(){
		master.tell(new StartSystemMsg(getMatrix(), getView(), stopFlag),
				ActorRef.noSender());

	}

	public void stopped() {
		master.tell(new StopMsg(),
				ActorRef.noSender());
	}

    public Matrix getMatrix() {
        return matrix;
    }

    public View getView() {
        return view;
    }
}
