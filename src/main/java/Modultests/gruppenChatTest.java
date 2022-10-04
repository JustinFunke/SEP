package Modultests;

import Client.ProjektGruppen.Chats;
import org.junit.jupiter.api.BeforeAll;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
public class gruppenChatTest {

    static Chats chat;
    int pgID = 42;
    int userID = 1000007;
    ArrayList<String> meineNachrichten = new ArrayList<>();

    @BeforeAll
    public static void before() {
        chat = new Chats();
    }

    @Test
    public void testChat() {
        meineNachrichten.add("Hi");
        meineNachrichten.add("wie gehts?");
        meineNachrichten.add("gut und dir?");
        meineNachrichten.add("ja muss");

        chat.NachrichtSenden("Hi", pgID, userID);
        chat.NachrichtSenden("wie gehts?", pgID, userID);
        chat.NachrichtSenden("gut und dir?", pgID, userID);
        chat.NachrichtSenden("ja muss", pgID, userID);
        assertEquals(meineNachrichten, Chats.NachrichtenAnzeigen(pgID));
    }

}
