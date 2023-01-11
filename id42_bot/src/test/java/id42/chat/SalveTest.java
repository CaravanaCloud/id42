package id42.chat;

import id42.bot.Listener;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static id42.bot.ChatIntent.State.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class SalveTest extends ChatTest {
    @Inject
    Listener listener;

    @Test
    public void testSalve() {
        var outcome = listener.ingest(identity(),
                sessionId(),
                "/salve");
        assertEquals(READY, outcome.state());
    }
}
