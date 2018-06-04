package exercize02.model.messages;

import akka.actor.ActorRef;
import exercize02.model.actors.User;
import javafx.collections.ObservableList;

public class AddActorButtonPressedMsg {

    private final ObservableList<ActorRef> users;

    public AddActorButtonPressedMsg(final ObservableList<ActorRef> users) {
        this.users = users;
    }

    public ObservableList<ActorRef> getUsers() {
        return this.users;
    }
}