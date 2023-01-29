package id42.service.chat;

import id42.chat.ChatRequest;
import id42.chat.IntentKey;
import id42.chat.SlotKey;
import id42.entity.ChatSession;
import id42.entity.DeliveriesRequest;
import id42.entity.Delivery;
import id42.intent.ID42Intents;
import id42.service.DeliveryService;
import id42.service.Service;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
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
    BeanManager bm;


    public void init(@Observes StartupEvent evt) {
        intentConsumers.put(ID42Intents.RequestDeliveries, this::requestDeliveries);
    }

    @Transactional
    public void accept(ChatRequest chat) {
        var sessionId = chat.sessionId();
        var intent = chat.intent();
        log.info("Accepting chat on session [{}] witn intent [{}]", sessionId, intent);
        var session = getSession(sessionId);
        var consumer = intentConsumers.get(intent);
        if (consumer != null) {
            log.trace("Consumer found.");
            consumer.accept(session, chat);
            bm.fireEvent(chat, null);
        } else {
            log.warn("Consumer not found");
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


}
