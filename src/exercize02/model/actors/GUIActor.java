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

import java.util.Map;

public class GUIActor extends AbstractActor {

    private final ObservableList<ActorRef> users;
    private final ActorRef registry;
    private Map<ActorRef, ObservableList<String>> mapOfChats;

    public GUIActor(final ObservableList<ActorRef> users, final Map<ActorRef, ObservableList<String>> mapOfChats) {
        this.users = users;
        this.mapOfChats = mapOfChats;
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
                final String actorName = ActorsUtility.generateActorName();
                final ActorRef newActor = getContext().getSystem().actorOf(
                        Props.create(User.class, actorName), actorName);
                registry.tell(new AddActorButtonPressedMsg(), newActor);
                newActor.tell(new StartUser(registry, getSelf()), ActorRef.noSender());

                this.mapOfChats.putIfAbsent(newActor, FXCollections.observableArrayList());

                boolean start = users.size() == 0;
                this.users.add(newActor);

                if (start) registry.tell(new PassToken(), ActorRef.noSender());

            });
        }).match(RemActorButtonPressedMsg.class, msg -> {
            msg.getToBeRemoved().tell(new RemActorButtonPressedMsg(), getSelf());

        }).match(CanExit.class, canExit -> {
            Platform.runLater(() -> this.users.remove(canExit.getToBeRemoved()));
        }).match(GUIShowMsg.class, msg -> {
            /*
                Appoggio il riferimento all'attore mittente,
                altrimenti nel runLater() va a rifersi all'attore gestore delle deadLetters
            */
            final ActorRef sender = getSender();

            Platform.runLater(() -> this.mapOfChats.get(sender).add(msg.getFullMsg()));

            getSender().tell(new GUIAcknowledgeMsg(msg.getMsg(), msg.getSender()), getSelf());
        }).match(ActorSelectedMsg.class, msg -> {
            //TODO cosa fare quando nella GUI viene selezionato un attore dalla Lista.
        }).build();
    }
}
