package id42.bot.lex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import id42.Identity;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class InputTest {
    
    @Test
    public void testCase_00(){
        Identity identity = new Identity() {};
        var input = new Input(identity, "123456","command inicialize");
    }

    @Test
    public void testCase_01(){
        Identity identity = new Identity() {};
        var input = new Input(identity, "123456","/start_bot");
    }

    @Test//Check the method of in Input class. If the parameter text is equal to null, null is returned
    public void testOf_00(){
        Identity identity = new Identity() {};
        var resultInput = Input.of(identity, "123456",null);
        assertEquals(null, resultInput);
    }

    @Test
    public void testOf_01(){
        Identity identity = new Identity() {};    
        var resultInput = Input.of(identity, "123456", "/start_bot");
        assertEquals("123456", resultInput.sessionId());
        assertEquals("/start_bot", resultInput.text());
    }

    @Test
    public void prompt_Test(){
        Identity identity = new Identity() {};
        var input = new Input(identity, "78985", "start bot");
        assertEquals("start bot", input.prompt());
    }

    @Test//test if the command field
    public void command_test00(){
        Identity identity = new Identity() {};
        var input = new Input(identity, "78985", "/start request");
        assertEquals("/start", input.command());
    }

    @Test//test if the command field
    public void command_test01(){
        Identity identity = new Identity() {};
        var input = new Input(identity, "78985", "start request");
        assertEquals("", input.command());
    }

}
