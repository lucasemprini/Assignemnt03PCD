package exercize01.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize01.model.Flag;
import exercize01.model.InputListener;
import exercize01.model.Matrix;
import exercize01.model.actors.Master;
import exercize01.model.messages.StartSystem;
import exercize01.model.messages.StopMsg;
import exercize01.model.messages.UpdateGUIMsg;
import exercize01.view.View;
import javafx.scene.paint.Stop;

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
	
	public void started(){
		master.tell(new StartSystem(getMatrix(), getView(), stopFlag),
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
