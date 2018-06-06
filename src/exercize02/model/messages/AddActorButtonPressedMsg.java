package exercize02.model.messages;

import akka.actor.ActorRef;
import akka.actor.dsl.Creators;
import exercize02.model.utility.ActorsUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Notifica l'aggiunta di un attore alla chat.
 */
public class AddActorButtonPressedMsg {

    private final ObservableList<ActorRef> users;
    private final String actorName;

    public AddActorButtonPressedMsg(final ObservableList<ActorRef> users, final String actorName) {
        this.users = users;
        this.actorName = actorName;
    }

    public AddActorButtonPressedMsg(String actorName) {
        this.actorName = actorName;
        this.users = FXCollections.observableArrayList();
    }
    public AddActorButtonPressedMsg() {
        this.actorName = ActorsUtility.generateActorName();
        this.users = FXCollections.observableArrayList();
    }

    public ObservableList<ActorRef> getUsers() {
        return this.users;
    }

    public String getActorName() {
        return actorName;
    }
}
