package id42.chat;

import id42.chat.bot.Listener;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static id42.chat.bot.Outcome.Type.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class SalveTest extends ChatTest {
    @Inject
    Listener listener;

    @Test
    public void testSalve() {
        var outcome = listener.ingest(identity(), "/salve");
        assertEquals(READY, outcome.type());
    }
}
