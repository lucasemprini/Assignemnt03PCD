package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import exercize02.model.messages.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class User extends AbstractActorWithStash {

    private final String name;
    private boolean token = false;



    private boolean waiting = false;
    private int nWaiting = 0;
    private int counter = 0;
    private ActorRef registry;
    private ActorRef guiActor;
    private final Queue<String> buffer = new LinkedList<>();
    private List<ActorRef> otherActors;
    private boolean wantExit = false;
    public User(final String name) {
        this.name = name;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StartUser.class, startUser -> {
            registry = startUser.getRegistry();
            guiActor = startUser.getGuiActor();
            stash();
        }).match(SendMsg.class, msg -> {
            buffer.add(msg.getMessage());
        }).match(RemActorButtonPressedMsg.class, remActorButtonPressedMsg -> {
            wantExit = true;
        }).match(TakeToken.class, takeToken -> {
            takeToken();

            registry.tell(new GetMeOthers(), ActorRef.noSender());

        }).match(OtherActors.class, otherActors -> {
            this.otherActors = otherActors.getActors();
            getSelf().tell(new SendBroadcastMsg(buffer.poll(), this.otherActors), ActorRef.noSender());
        }).match(SendBroadcastMsg.class, sendBroadcastMsg -> {
            if (sendBroadcastMsg.getMsg() != null) {
                setWaitingActors(sendBroadcastMsg.getActors().size());
                sendBroadcastMsg.getActors().forEach(actorRef -> actorRef.tell(new ShowMsg(sendBroadcastMsg.getMsg()), getSelf()));
            } else {
                getSelf().tell(new TerminateUserOperation(), ActorRef.noSender());
            }
        }).match(AcknowledgeMsg.class, acknowledgeMsg -> {
            incCounter();

            if (getCounter() >= getWaitingActors()) {
                reset();
                guiActor.tell(new GUIShowMsg(acknowledgeMsg.getMsg(), getSelf()), getSelf());
            }
        }).match(GUIAcknowledgeMsg.class, guiAcknowledgeMsg -> {
            if (guiAcknowledgeMsg.getSender() == getSelf()) {
                getSelf().tell(new SendBroadcastMsg(buffer.poll(), otherActors), ActorRef.noSender());
            } else {
                guiAcknowledgeMsg.getSender().tell(new AcknowledgeMsg(guiAcknowledgeMsg.getMsg()), getSelf());
            }
        }).match(ShowMsg.class, showMsg -> {
            guiActor.tell(new GUIShowMsg(showMsg.getMsg(), getSender()), getSelf());
        }).match(TerminateUserOperation.class, terminateUserOperation -> {
            if (wantExit) {
                registry.tell(RemActorButtonPressedMsg.class, getSelf());
                wantExit = false;
            } else {
                passToken();
            }
        }).match(CanExit.class, canExit -> {
            guiActor.tell(new CanExit(), ActorRef.noSender());
            passToken();
        }).build();
    }

    public String getName() {
        return this.name;
    }

    public boolean hasToken() {
        return token;
    }

    public void takeToken() {
        unstashAll();
        this.token = true;
    }

    private void passToken() {
        registry.tell(new PassToken(), getSelf());
        this.token = false;
    }

    private boolean isWaiting() {
        return waiting;
    }

    private void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    private int getWaitingActors() {
        return nWaiting;
    }

    private void setWaitingActors(int nWaiting) {
        if (nWaiting > 0 ) {
            setWaiting(true);
        }
        this.nWaiting = nWaiting;
    }

    private void incCounter() {counter++;}

    private int getCounter() {return  counter;}

    private void reset() {
        counter = 0;
        setWaitingActors(0);
        setWaiting(false);
    }
}
