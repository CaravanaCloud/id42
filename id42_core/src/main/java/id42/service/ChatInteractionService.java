package id42.service;

import id42.chat.ChatRequest;
import id42.chat.IntentKey;
import id42.chat.SlotKey;
import id42.entity.ChatSession;
import id42.entity.DeliveriesRequest;
import id42.entity.Delivery;
import id42.intent.ID42Intents;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static id42.intent.DeliverySlot.*;
import static id42.intent.DeliverySlot.locator;

@ApplicationScoped
public class ChatInteractionService extends Service {
    @Inject
    EntityManager em;

    @Inject
    DeliveryService deliveryService;

    Map<IntentKey, BiConsumer<ChatSession, ChatRequest>> intentConsumers = new HashMap<>();

    public void init(@Observes StartupEvent evt) {
        intentConsumers.put(ID42Intents.RequestDeliveries, this::requestDeliveries);
    }

    @Transactional
    public void accept(ChatRequest chat) {
        var session = getSession(chat.sessionId());
        var intent = chat.intent();
        var consumer = intentConsumers.get(intent);
        if (consumer != null) {
            log.info("Resolved intent: {}", intent);
            consumer.accept(session, chat);
        } else {
            log.warn("No consumer for intent: {} ", intent);
        }
    }

    private ChatSession getSession(String sessionId) {
        log.info("Loading session: {}", sessionId);
        var sessions = em.createNamedQuery("ChatSession.bySessionId", ChatSession.class)
                .setParameter("sessionId", sessionId)
                .getResultStream()
                .findFirst();
        var session = sessions.orElseGet(() -> {
            var newSession = new ChatSession(sessionId);
            em.persist(newSession);
            return newSession;
        });
        session.touch();
        return session;
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
