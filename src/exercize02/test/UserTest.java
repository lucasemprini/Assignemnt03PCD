package exercize02.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import exercize02.model.actors.GUIActor;
import exercize02.model.actors.Register;
import exercize02.model.actors.User;
import exercize02.model.messages.AddActorButtonPressedMsg;
import exercize02.model.messages.GetMeOthers;
import exercize02.model.messages.OtherActors;
import exercize02.model.messages.RemActorButtonPressedMsg;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class UserTest {

    private TestKit kit;
    private ActorRef registry;
    private ActorRef chatOne;
    private ActorRef chatTwo;
    private ActorSystem system;
    private TestKit testUnit;
    private ActorRef guiActor;

    public UserTest() {

    }

    @Before
    public void setUp() throws Exception {
        this.system = ActorSystem.create("MySystem");
        this.registry = system.actorOf(Props.create(Register.class));
        this.testUnit = new TestKit(system);
        this.guiActor = system.actorOf(Props.create(GUIActor.class, null, null));
    }


    /**
     * Aggiunta di due attori al registro e visualizzazione del corretto funzionamento del GetMeOthers
     * @throws Exception
     */
    @Test
    public void addActorToRegistryAndGet() throws Exception {
        chatOne = system.actorOf(Props.create(User.class, "Chat1"));
        chatTwo = system.actorOf(Props.create(User.class, "Chat2"));
        Thread.sleep(100);
        registry.tell(new AddActorButtonPressedMsg(), chatOne);
        registry.tell(new AddActorButtonPressedMsg(), chatTwo);

        registry.tell(new GetMeOthers(), testUnit.getRef());
        OtherActors msg = testUnit.expectMsgClass(OtherActors.class);

        assertEquals(2, msg.getActors().size());
        assertEquals(chatOne, msg.getActors().get(0));
    }


    /**
     * Test del corretto funzionamento della rimozione di un attore dal registry
     * @throws Exception
     */
    @Test
    public void removeActorToRegistry() throws Exception {
        setUp();
        chatOne = system.actorOf(Props.create(User.class, "Chat1"));
        chatTwo = system.actorOf(Props.create(User.class, "Chat2"));
        registry.tell(new AddActorButtonPressedMsg(), chatOne);
        registry.tell(new AddActorButtonPressedMsg(), chatTwo);
        Thread.sleep(100);
        registry.tell(new RemActorButtonPressedMsg(chatOne), chatOne);
        Thread.sleep(100);

        registry.tell(new GetMeOthers(), testUnit.getRef());
        OtherActors msg = testUnit.expectMsgClass(OtherActors.class);

        assertEquals(1, msg.getActors().size());
        assertEquals(chatTwo, msg.getActors().get(0));
    }
}
