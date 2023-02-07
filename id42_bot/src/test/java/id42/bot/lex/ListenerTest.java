package id42.bot.lex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import id42.Identity;

import javax.inject.Inject;

import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ListenerTest {
    @Inject
    Listener listener;

    @Test
    public void testToJSON_01(){
        var update = new Update();
        var json = listener.toJson(update);
        System.out.println(json);
        assertNotNull(json);
    }

    @Test
    public void testToJSON_02(){
        var update = new Update();
        var json = listener.toJson(update);
        assertEquals("{}", json);
    }

    @Test
    public void messageTest_01(){
        var update = new Update();
        var message = listener.message(update);
        assertNull(message);
    }

    @Test
    public void messageTest_02(){
        var update = new Update();
        var message = new Message();
            
        
        update.setMessage(message);
        var result = listener.message(update);
        assertNotNull(result);
    }

    @Test
    public void getBotUsernameTest(){
        String botName = listener.getBotUsername();
        assertEquals("id42_newmember_bot", botName);
    }

    @Test
    public void sendTextTest_01(){
        Long who = 1234L;
        String what = null;

        listener.sendText(who, what);
    }

    @Test
    public void sendTextTest_02(){
        Long who = null;
        String what = "";

        listener.sendText(who, what);
    }

    @Test
    public void sendTextTest_03(){
        Long who = null;
        String what = null;

        listener.sendText(who, what);
    }

    //replyFrom tests

    @Test//replyFrom method with the message parameter with the field User equals to null
    public void replyFromTest_00(){
        var message = new Message();
        assertThrows(NullPointerException.class, () -> {
            listener.replyFrom(message, "Test");
        });
    }

    //@Test//replyFrom method with the message parameter with the field User equals to null
    /*public void replyFromTest_01(){
        var message = new Message();
        User user = new User(1234L, "bot_test", true);
        message.setFrom(user);
        listener.replyFrom(message, "message of replyFromTest_04");
    }*/

    //replyChat tests

    @Test//replyChat method with the chat object with the field chat equals to null
    public void replyChatTest_00(){
        var message = new Message();
        assertThrows(NullPointerException.class, () -> {
            listener.replyChat(message, "Test");
        });
    }


    //Tests of the method handleRequest(Input input)
    @Test
    public void handleRequestText_00(){
        var input = new Input(null, null, "test");
        listener.handleRequest(input);
    }

    //Tests of onUpdateReceived method
    @Test
    public void onUpdateReceivedTest(){
        Update update = new Update();
        listener.onUpdateReceived(update);
    }


    //Tests of handleRequest(identity, sessionId, text)
    @Test
    public void handleRequestTest_00(){
        var result = listener.handleRequest(null, null, null);
        assertEquals(null, result);
    }

    @Test
    public void handleRequestTest_01(){
        var result = listener.handleRequest(null, null, "");
        assertEquals(null, result);
    }

    @Test//Test what happens if identity and sessionId parameters are equal to nullç
    public void handleRequestTest_02(){
        var result = listener.handleRequest(null, null, "message from handleRequestTest_02");

    }

}
