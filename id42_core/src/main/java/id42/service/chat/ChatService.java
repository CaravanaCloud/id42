package id42.service.chat;

import id42.chat.ChatRequest;
import id42.entity.ChatSession;
import id42.service.Service;
import id42.to.ChatResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class ChatService extends Service {
    @Inject
    EntityManager em;
    @Inject
    Event<ChatEvent> chatEvent;

    @Transactional
    public ChatResponse accept(ChatRequest chat) {
        var response = ChatResponse.empty();
        var sessionId = chat.sessionId();
        var intent = chat.intent();
        log().info("Accepting chat on session [{}] witn intent [{}]", sessionId, intent);
        var session = getSession(sessionId);
        var event = ChatEvent.of(session, chat);
        chatEvent.fire(event);
        log().trace("Chat event fired: {}", event);
        return response;
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
