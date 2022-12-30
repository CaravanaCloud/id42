package id42.chat;

import id42.chat.bot.HAL;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;

@QuarkusTest
public class HALTest {
    @Inject
    HAL hal;

    @Test
    public void testAskAnything() {
        var response = hal.ask("How are you doing?");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(!response.isEmpty());
    }

    @Test
    public void testAskSetDeliveryContact() {
        var response = hal.ask("...set delivery contact Fujiro Nakombi");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(!response.isEmpty());
    }
}
