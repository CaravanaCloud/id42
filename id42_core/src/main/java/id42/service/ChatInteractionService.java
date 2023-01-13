package id42.service;

import id42.chat.ChatRequest;
import id42.chat.IntentKey;
import id42.entity.ChatSession;
import id42.entity.Delivery;
import id42.intent.ID42Intents;
import id42.intent.ID42Slots;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@ApplicationScoped
public class ChatInteractionService extends Service {
    @Inject
    EntityManager em;

    Map<IntentKey, Consumer<ChatRequest>> intentConsumers = new HashMap<>();

    public void init(@Observes StartupEvent evt){
        intentConsumers.put(ID42Intents.RequestDeliveries, this::requestDeliveries);
    }

    @Transactional
    public void accept(ChatRequest chat){
        var session = getSession(chat.sessionId());
        var intent = chat.intent();
        var consumer = intentConsumers.get(intent);
        if(consumer != null){
            log.info("Resolved intent: {}", intent);
            consumer.accept(chat);
        }else {
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
        return session;
    }

    private ChatRequest requestDeliveries(ChatRequest chat) {
        var sessionId = chat.sessionId();
        log.info("Requesting deliveries for session {}", sessionId);
        var deliveries = chat.slotList(Delivery.class, ID42Slots.deliveries);
        log.info("Found " + deliveries.size() + " deliveries");
        return chat;
    }

}
