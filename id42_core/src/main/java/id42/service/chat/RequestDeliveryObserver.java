package id42.service.chat;

import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import id42.chat.ChatRequest;
import id42.chat.SlotKey;
import id42.entity.ChatSession;
import id42.entity.DeliveriesRequest;
import id42.entity.Delivery;
import id42.service.DeliveryService;

@ApplicationScoped
public class RequestDeliveryObserver {
    @Inject
    EntityManager em;

    @Inject
    DeliveryService deliveryService;

    

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
        tryUpdate(chat, pickLocation, delivery::pickLocation);
        tryUpdate(chat, pickSpot, delivery::pickSpot);
        tryUpdate(chat, pickContact, delivery::pickContact);
        tryUpdate(chat, dropLocation, delivery::dropLocation);
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
        var delivery = Delivery.of(request, null, null, null, null, null, null, null);
        return delivery;
    }

}
