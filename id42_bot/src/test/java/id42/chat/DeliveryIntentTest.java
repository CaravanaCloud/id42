package id42.chat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
public class DeliveryIntentTest {
    @Inject
    HAL hal;

    @Inject
    Logger log;

    @Test
    public void testCase1(){
        var prompt = """
                Programa
                                
                Jueves 22/11/2021
                                
                7:30 - Entrega Erico & Benjamina a Muntaner 123
                """;
        var outcome = hal.ask(prompt);
        debug(prompt, outcome);
        assertFalse(outcome.isBlank());
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
