package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import exercize02.Main;
import exercize02.model.messages.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class User extends AbstractActorWithStash {

    private static final int TOKEN_TIME = 2500;
    private long startTime;

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
            log("Messaggio inserito nel buffer");
            buffer.add(msg.getMessage());
        }).match(RemActorButtonPressedMsg.class, remActorButtonPressedMsg -> {
            wantExit = true;
        }).match(TakeToken.class, takeToken -> {
            takeToken();
            registry.tell(new GetMeOthers(), getSelf());
        }).match(OtherActors.class, otherActors -> {
            this.otherActors = otherActors.getActors();
            getSelf().tell(new SendBroadcastMsg(buffer.poll(), this.otherActors), ActorRef.noSender());
        }).match(SendBroadcastMsg.class, sendBroadcastMsg -> {
            if (sendBroadcastMsg.getMsg() != null) {
                setWaitingActors(sendBroadcastMsg.getActors().size() + 1);
                sendBroadcastMsg.getActors().forEach(actorRef -> {
                    log("Send msg to: " + actorRef.toString());
                    actorRef.tell(new ShowMsg(sendBroadcastMsg.getMsg()), getSelf());

                });

                getSelf().tell(new AcknowledgeMsg(sendBroadcastMsg.getMsg()), getSelf());
            } else {
                getSelf().tell(new TerminateUserOperation(), ActorRef.noSender());
            }
        }).match(AcknowledgeMsg.class, acknowledgeMsg -> {
            incCounter();
            if (getCounter() >= getWaitingActors()) {
                final String msg = "Me: " + acknowledgeMsg.getMsg().split(":")[1];
                guiActor.tell(new GUIShowMsg(msg, getSelf()), getSelf());
                reset();
                log("Chiamo la GUI per renderizzare su di me");
            }
        }).match(GUIAcknowledgeMsg.class, guiAcknowledgeMsg -> {
            if (guiAcknowledgeMsg.getSender() == getSelf()) {
                getSelf().tell(new SendBroadcastMsg(buffer.poll(), otherActors), ActorRef.noSender());
            } else {
                guiAcknowledgeMsg.getSender().tell(new AcknowledgeMsg(guiAcknowledgeMsg.getMsg()), getSelf());
            }
        }).match(ShowMsg.class, showMsg -> {
            guiActor.tell(new GUIShowMsg(getSender().path().name()+ ": " +showMsg.getMsg(), getSender()), getSelf());
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
        startTime = System.currentTimeMillis();
    }

    private void passToken() {
        long sleep = TOKEN_TIME - (System.currentTimeMillis() - startTime);
        if (sleep > 0) {
            try {
                Thread.sleep(sleep);
            } catch (Exception ex) {
                log(ex.getMessage());
            }
        }
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

    private void log(final String msg) {
        if (Main.DEBUG) System.out.println("User " + getName() + " : " + msg);
    }
}
