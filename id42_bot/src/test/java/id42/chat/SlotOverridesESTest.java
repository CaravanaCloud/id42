package id42.chat;

import id42.intent.ID42Slots;
import id42.lex.SlotOverrides;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static id42.intent.ID42Slots.pickupContact;
import static id42.intent.ID42Slots.pickupTime;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SlotOverridesESTest {
    @Inject
    SlotOverrides overrides;

    @Test
    public void testePickupContact() {
        var text = "Cliente: Lance Armstrong";
        var chat = overrides.transform(text);
        var expectedText = "Cliente: Lance Armstrong";
        var expectedSlot = "Lance Armstrong";
        assertEquals(expectedText, chat.outputText());
        assertEquals(expectedSlot, chat.getString(pickupContact));
    }


    @Test
    public void testPickupTimeEmoji() {
        var text = "🕛 de la noche en Habana, cuba";
        var chat = overrides.transform(text);
        var expected = "12:00 de la noche en Habana, cuba";
        assertEquals(expected, chat.outputText());
        assertEquals("12:00", chat.getString(pickupTime));
    }

    @Test
    public void testPickupTimeText() {
        var text = "Hora: 16:20 manolos";
        var chat = overrides.transform(text);
        var expected = "Hora: 16:20 manolos";
        assertEquals(expected, chat.outputText());
        assertEquals("16:20", chat.getString(pickupTime));
    }




}
