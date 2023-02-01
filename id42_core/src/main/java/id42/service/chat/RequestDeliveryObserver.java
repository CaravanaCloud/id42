package id42.service.chat;

import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import id42.chat.ChatRequest;
import id42.chat.SlotKey;
import id42.entity.ChatSession;
import id42.entity.DeliveriesRequest;
import id42.entity.Delivery;
import id42.service.Observer;
import id42.service.DeliveryService;

import static id42.intent.DeliverySlot.*;
import static id42.intent.DeliverySlot.dropContact;


@ApplicationScoped
public class RequestDeliveryObserver extends Observer {

    @Inject
    DeliveryService deliveryService;

    private void onChat(@Observes ChatEvent chat){
        log().info("Chat event received: {}", chat);
        requestDeliveries(chat.session(), chat.request());
    }

    private ChatRequest requestDeliveries(ChatSession session,
            ChatRequest chat) {
        var request = session.deliveriesRequest();
        if (request == null) {
            request = DeliveriesRequest.of();
            request.persist();
            session.deliveriesRequest(request);
        }
        var deliveries = deliveryService.findByRequest(request);
        var delivery = (Delivery) null;
        if (deliveries.isEmpty()) {
            delivery = deliveryOf(request, chat);
            delivery.persist();
        } else {
            delivery = deliveries.get(0);
        }
        updateDelivery(delivery, chat);
        return chat;
    }

    private void updateDelivery(Delivery delivery, ChatRequest chat) {
        tryUpdate(chat, locator, delivery::locator);
        tryUpdate(chat, pickAddress, delivery::pickAddress);
        tryUpdate(chat, pickAddressDetail, delivery::pickAddressDetail);
        tryUpdate(chat, pickSpot, delivery::pickSpot);
        tryUpdate(chat, pickContact, delivery::pickContact);
        tryUpdate(chat, dropAddress, delivery::dropAddress);
        tryUpdate(chat, dropAddressDetail, delivery::dropAddressDetail);
        tryUpdate(chat, dropSpot, delivery::dropSpot);
        tryUpdate(chat, dropContact, delivery::dropContact);
        tryUpdate(chat, deliveryNote, delivery::deliveryNote);
        var _pickTime = chat.getString(pickTime);
        var _pickDate = chat.getString(pickDate);
        if (_pickTime != null && _pickDate != null) {
            delivery.pickTime(_pickDate, _pickTime);
        }
    }

    private void tryUpdate(ChatRequest chat,
            SlotKey key,
            Consumer<String> setter) {
        var value = chat.getString(key);
        if (value != null) {
            setter.accept(value);
        }
    }

    private Delivery deliveryOf(DeliveriesRequest request, ChatRequest chat) {
        var delivery = Delivery.of(request);
        return delivery;
    }

}
