package id42.service;

import id42.chat.ChatRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;

import static id42.intent.ID42Intents.RequestDeliveries;
import static id42.intent.DeliverySlot.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
public class ChatInteractionServiceTest {
    @Inject
    ChatInteractionService chatService;

    @Inject
    DeliveryService deliveryService;

    @Test
    public void testRequestDelivery0() {
        // https://martinfowler.com/bliki/GivenWhenThen.html
        // given
        var code = UUID.randomUUID().toString();
        // when
        var chat = ChatRequest.of(RequestDeliveries)
                .put(locator, code)
                .put(pickDate, "2020-01-01")
                .put(pickTime, "08:00")
                .put(pickLocation, "Av. Diagonal 123")
                .put(dropLocation, "Av. Diagonal 321");
        chatService.accept(chat);
        // then
        var delivery = deliveryService.findByLocator(code);
        delivery.ifPresentOrElse(
                d -> {
                    assertEquals("2020-01-01", d.pickupDateFmt());
                    assertEquals("08:00", d.pickupTimeFmt());
                    assertEquals("Av. Diagonal 123", d.pickupLocation());

                },
                () -> fail("Delivery not found"));
    }
}
