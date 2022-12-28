package id42.chat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class InputTest {
    @Test
    public void testInputWithCommandAndAlias() {
        var in = Input.of("/ask@id42_bot .set delivery contact Fujiro Nakombi");
        var prompt = in.prompt().text();
        var expected = ".set delivery contact Fujiro Nakombi";
        assertEquals(expected, prompt);
    }

    @Test
    public void testInputWithoutCommand() {
        var in = Input.of(".set delivery contact Fujiro Nakombi");
        var prompt = in.prompt().text();
        var expected = ".set delivery contact Fujiro Nakombi";
        assertEquals(expected, prompt);
    }

    @Test
    public void testSetCommand1() {
        var in = Input.of(".set delivery contact Fujiro Nakombi");
        var prompt = in.prompt();
        var expected = ".set delivery contact Fujiro Nakombi";
        assertEquals("set", prompt.action());
        assertEquals("delivery", prompt.object());
        assertEquals("contact", prompt.property());
        assertEquals("Fujiro Nakombi", prompt.arguments());
    }

    @Test
    public void testSalve() {
        var in = Input.of("/salve");
        var prompt = in.prompt();
        assertEquals("/salve", in.command());
    }
}
