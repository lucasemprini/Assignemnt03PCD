package exercize02.model.messages;

import akka.actor.ActorRef;
import exercize02.model.actors.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

/**
 * Notifica l'aggiunta di un attore alla chat.
 */
public class AddActorButtonPressedMsg {

    private final ObservableList<ActorRef> users;

    public AddActorButtonPressedMsg(final ObservableList<ActorRef> users) {
        this.users = users;
    }

    public AddActorButtonPressedMsg() {
        this.users = FXCollections.observableArrayList();
    }

    public ObservableList<ActorRef> getUsers() {
        return this.users;
    }
}
