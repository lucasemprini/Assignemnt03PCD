package exercize02.model.actors;

import akka.actor.AbstractActor;
import exercize02.model.messages.ActorsInRegistryAskMsg;
import exercize02.model.messages.AddActorButtonPressedMsg;
import exercize02.model.messages.RemActorButtonPressedMsg;

public class Register extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ActorsInRegistryAskMsg.class, msg -> {
            //TODO cosa fare quando si chiede la lista degli attori nel Registro.
        }).match(AddActorButtonPressedMsg.class, msg -> {
            //TODO cosa fare quando si chiede di aggiungere un attore al Registro.
        }).match(RemActorButtonPressedMsg.class, msg -> {
            //TODO cosa fare quando si chiede di rimuovere un attore dal Registro.
        }).build();
    }
}