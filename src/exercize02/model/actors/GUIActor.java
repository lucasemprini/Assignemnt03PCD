package exercize02.model.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import exercize02.model.messages.*;
import exercize02.model.utility.ActorsUtility;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.util.Map;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;
    private ObservableList<String> currentChat;
    private final ActorRef registry;
    private Map<ActorRef, ObservableList<String>> mapOfChats;
    private final Label actorLabel;

    public GUIActor(final ObservableList<ActorRef> users,
                    final Map<ActorRef, ObservableList<String>> mapOfChats,
                    final ObservableList<String> currentChat,
                    final Label actorLabel) {
        this.users = users;
        this.mapOfChats = mapOfChats;
        this.currentChat = currentChat;
        this.actorLabel = actorLabel;
        registry = ActorSystem.create("MySystem").actorOf(Props.create(Register.class));
    }

    /**
     *  Funzionalità per ogni messaggio:
     *      - SendButtonMessageMsg
     *          notifica all'attore selezionato la volontà di inviare un messaggio
     *      - AddActorButtonPressedMsg
     *          creazione di un attore e inserimento nella chat.
     *      - RemActorButtonPressedMsg
     *          notifica all'attore selezionato la volontà di uscire dalla chat
     *      - CanExit
     *          conferma la possibilità di uscire dalla chat da parte di un attore
     *      - GUIShowMsg
     *          richiesta di visualizzazione di un msg da parte di un attore
     *      - ActorSelectedMsg
     *          TODO non usato..
     * @return
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(SendButtonMessageMsg.class, msg -> {
            msg.getSender().tell(new SendMsg(msg.getMessage(), msg.getListOfMessages()), getSender());
        }).match(AddActorButtonPressedMsg.class, msg -> {
            Platform.runLater(() -> {
                final String actorName = msg.getActorName();
                final ActorRef newActor = getContext().getSystem().actorOf(
                        Props.create(User.class, actorName), actorName);
                registry.tell(new AddActorButtonPressedMsg(actorName), newActor);
                newActor.tell(new StartUser(registry, getSelf()), ActorRef.noSender());

                this.mapOfChats.putIfAbsent(newActor, FXCollections.observableArrayList());

                boolean start = users.size() == 0;
                this.users.add(newActor);

                if (start) newActor.tell(new TakeToken(0), ActorRef.noSender());

            });
        }).match(RemActorButtonPressedMsg.class, msg -> {
            msg.getToBeRemoved().tell(new RemActorButtonPressedMsg(), getSelf());

        }).match(CanExit.class, canExit -> {
            Platform.runLater(() -> {
                    final ActorRef toBeRemoved = canExit.getToBeRemoved();
                    this.users.remove(toBeRemoved);
                    System.out.println("Removing chat: " + this.currentChat);
                    this.currentChat.clear();
                    System.out.println("Removing chat: " + this.currentChat);
                    this.mapOfChats.remove(toBeRemoved);

            });
        }).match(GUIShowMsg.class, msg -> {
            /*
                Appoggio il riferimento all'attore mittente,
                altrimenti nel runLater() va a rifersi all'attore gestore delle deadLetters
            */
            final ActorRef sender = getSender();

            Platform.runLater(() -> {
                this.mapOfChats.get(sender).add(msg.getFullMsg());
                System.out.println("Map of chats.get(sender) = "
                        + this.mapOfChats.get(sender)
                        + "\nSender: " + sender
                        + "\nmessage: " + msg.getFullMsg());
            });


            getSender().tell(new GUIAcknowledgeMsg(msg.getMsg(), msg.getSender()), getSelf());
        }).match(ActorSelectedMsg.class, msg -> {
            Platform.runLater(() -> {
                this.currentChat = mapOfChats.get(msg.getSelected());
                this.actorLabel.setText(msg.getSelected().path().name() + " says:");
            });
        }).build();
    }
}
