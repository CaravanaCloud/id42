package id42.chat;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
@QuarkusTest
public class HALTest {
    @Inject
    HAL hal;

    @Test
    public void testAsk() {
        var response = hal.ask("How are you doing?");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(!response.isEmpty());
    }
}
