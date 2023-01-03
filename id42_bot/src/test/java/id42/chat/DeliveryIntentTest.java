package id42.chat;

import id42.chat.bot.Input;
import id42.chat.bot.Listener;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Chat;

import javax.inject.Inject;

import static id42.chat.bot.Outcome.Type.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
public class DeliveryIntentTest extends ChatTest {
    @Inject
    Logger log;

    @Test
    public void testCase1(){
        var prompt = """
                Programa
                                
                Jueves 22/11/2021
                                
                7:30 - Entrega Erico & Benjamina a Muntaner 123
                """;
        var outcome = ask(prompt);
        var message = outcome.message();
        debug(prompt, message);
        assertFalse(message.isBlank());
        assertEquals(READY, outcome.type());
    }

    private void debug(String prompt, String outcome) {
        var msg = new StringBuilder();
        msg.append("\n");
        msg.append("> ");
        msg.append(prompt);
        msg.append("\n");
        msg.append("< ");
        msg.append(outcome);
        msg.append("\n");
        log.info(msg.toString());
    }
}
