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
import javax.enterprise.event.Event;
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
    EntityManager em;
    @Inject
    Event<ChatEvent> chatEvent;

    @Transactional
    public void accept(ChatRequest chat) {
        var sessionId = chat.sessionId();
        var intent = chat.intent();
        log().info("Accepting chat on session [{}] witn intent [{}]", sessionId, intent);
        var session = getSession(sessionId);
        var event = ChatEvent.of(session, chat);
        chatEvent.fire(event);
        log().trace("Chat event fired: {}", event);
    }

    private ChatSession getSession(String sessionId) {
        log().info("Loading session: {}", sessionId);
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
