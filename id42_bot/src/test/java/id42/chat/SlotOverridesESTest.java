package id42.chat;

import id42.bot.SlotOverrides;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SlotOverridesESTest {
    @Inject
    SlotOverrides overrides;

    @Test
    public void testePickupContact() {
        var text = "Cliente: Lance Armstrong";
        var tx = overrides.transform(text);
        var expectedText = "Cliente: Lance Armstrong";
        var expectedSlot = "Lance Armstrong";
        assertEquals(expectedText, tx.outputText());
        assertEquals(expectedSlot, tx.slots().get("pickupContact"));
    }


    @Test
    public void testPickupTimeEmoji() {
        var text = "🕛 de la noche en Habana, cuba";
        var tx = overrides.transform(text);
        var expected = "12:00 de la noche en Habana, cuba";
        assertEquals(expected, tx.outputText());
        assertEquals("12:00", tx.slots().get("pickupTime"));
    }

    @Test
    public void testPickupTimeText() {
        var text = "Hora: 16:20 manolos";
        var tx = overrides.transform(text);
        var expected = "Hora: 16:20 manolos";
        assertEquals(expected, tx.outputText());
        assertEquals("16:20", tx.slots().get("pickupTime"));
    }




}
