package id42.chat;

import id42.bot.lex.SlotOverrides;
import id42.intent.ID42Slots;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static id42.intent.DeliverySlot.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SlotOverridesESTest {
    @Inject
    SlotOverrides overrides;

    @Test
    public void testePickContact() {
        var text = "Cliente: Lance Armstrong";
        var chat = overrides.transform(text);
        var expectedText = "Cliente: Lance Armstrong";
        var expectedSlot = "Lance Armstrong";
        assertEquals(expectedText, chat.outputText());
        assertEquals(expectedSlot, chat.getString(pickContact));
    }


    @Test
    public void testPickTimeEmoji() {
        var text = "🕛 de la noche en Habana, cuba";
        var chat = overrides.transform(text);
        var expected = "12:00 de la noche en Habana, cuba";
        assertEquals(expected, chat.outputText());
        assertEquals("12:00", chat.getString(pickTime));
    }

    @Test
    public void testPickTimeText() {
        var text = "Hora: 16:20 manolos";
        var chat = overrides.transform(text);
        var expected = "Hora: 16:20 manolos";
        assertEquals(expected, chat.outputText());
        assertEquals("16:20", chat.getString(pickTime));
    }




}
