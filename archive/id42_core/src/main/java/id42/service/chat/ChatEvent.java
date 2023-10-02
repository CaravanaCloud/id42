package id42.service.chat;

import id42.chat.ChatRequest;
import id42.entity.ChatSession;

public class ChatEvent {
    private final ChatSession session;
    private final ChatRequest request;

    public ChatEvent(ChatSession session, ChatRequest request) {
        this.session = session;
        this.request = request;
    }

    public static ChatEvent of(ChatSession session, ChatRequest request) {
        return new ChatEvent(session, request);
    }

    public ChatSession session() {
        return session;
    }

    public ChatRequest request() {
        return request;
    }
}
