package id42.chat;

import id42.chat.bot.SlotOverrides;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SlotOverridesESTest {
    @Inject
    SlotOverrides slotOverrides;

    @Test
    public void testePickupContact() {
        var text = "Cliente: José das Clouves";
        var transformed = slotOverrides.transform(text);
        assertEquals(text, transformed);
    }


    @Test
    public void testPickupTime() {
        var text = "la hora: \uD83D\uDD5B en la noche";
        var transformed = slotOverrides.transform(text);
        var expected = "la hora: 12:00 en la noche";
        System.out.println(text);
        System.out.println(transformed);
        assertEquals(expected, transformed);
    }




}
