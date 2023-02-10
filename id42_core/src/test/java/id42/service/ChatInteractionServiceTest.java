package id42.service;

import id42.chat.ChatRequest;
import id42.chat.Slots;
import id42.entity.Delivery;
import id42.entity.DeliveryState;
import id42.entity.ValidationState;
import id42.service.chat.ChatService;
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
    ChatService chatService;

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
                .put(pickAddress, "Av. Diagonal 123")
                .put(dropLocation, "Av. Diagonal 321");
        chatService.accept(chat);
        // then
        var delivery = deliveryService.findByLocator(code);
        delivery.ifPresentOrElse(
                d -> {
                    assertEquals("2020-01-01", d.pickDateFmt());
                    assertEquals("08:00", d.pickTimeFmt());
                    assertEquals("Av. Diagonal 123", d.pickAddress());
                },
                this::failDeliveryNotFound);
    }


    public void testRequestDeliveryChatWithStateChange(){
        //given
        var msg0 = (ChatRequest) null; // "programar entrega"; //TODO: converter pra ChatRequest correspondete
        chatService.accept(msg0);
        var msg1 = (ChatRequest) null; //"localizador 12345";
        chatService.accept(msg0);
        var msg2 = (ChatRequest) null; //"para hoy";
        chatService.accept(msg0);
        var msg3 = (ChatRequest) null; //"a las 10:00";
        chatService.accept(msg0);
        var msg4 = (ChatRequest) null; //"recogida en av diagonal 123";
        chatService.accept(msg0);
        var msg5 = (ChatRequest) null; //"entrega en av meridiana 321";
        chatService.accept(msg0);

        //when
        var request = (ChatRequest) ChatRequest.of(RequestDeliveries)
                .put(locator, UUID.randomUUID().toString())
                .put(pickDate, "2020-01-01")
                .put(pickTime, "08:00")
                .put(pickAddress, "Av. Diagonal 123")
                .put(dropAddress, "Av. Diagonal 321");
        var response = chatService.accept(request);
        var delivery = response.get(Slots.delivery, Delivery.class);
        var state = delivery.state();
        var validation = delivery.validation();
        
        //then
        assertEquals(DeliveryState.requested, state);
        assertEquals(ValidationState.valid, validation);
    }

    public void testRequestDeliveryChatWithInsufficientData(){
        //given
        var msg0 = (ChatRequest) null; // "programar entrega"; //TODO: converter pra ChatRequest correspondete
        chatService.accept(msg0);
        var msg1 = (ChatRequest) null; //"localizador 123456";
        chatService.accept(msg0);
        var msg2 = (ChatRequest) null; //"para hoy";
        chatService.accept(msg0);
        var msg3 = (ChatRequest) null; //"a las 10:00";
        chatService.accept(msg0);
        var msg4 = (ChatRequest) null; //"recogida en av diagonal 123";
        chatService.accept(msg0);

        //when
        var msgN = (ChatRequest) null; // "consultar entrega con localizador 1234"; //TODO: converter pra ChatRequest correspondete
        var delivery = (Object) null; // chatService.accept(msg0); //TODO: Julio: Refactor accept() to return data
        var state = (Object) null;
        var validation = (ValidationState) null;

        //then
        assertEquals(DeliveryState.created, state);
        assertEquals(ValidationState.invalid, validation);
    }

    void failDeliveryNotFound(){
        fail("Delivery not found");
    }
}
