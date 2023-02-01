package id42.bot.lex;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ListenerTest {
    @Inject
    Listener listener;

    @Test
    public void testToJSON(){
        var update = new Update();
        var json = listener.toJson(update);
        System.out.println(json);
        assertNotNull(json);
    }
}
