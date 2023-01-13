package id42.chat;

import id42.Identity;
import id42.lex.Listener;

import javax.inject.Inject;
import java.util.UUID;

public abstract class ChatTest {
    @Inject
    Listener listener;
    protected Identity identity() {
        return TestIdentity.anonymous();
    }


    protected ChatRequest ask(String prompt) {
        return listener.handleRequest(identity(),
                sessionId(),
                "/ask "+prompt);
    }

    protected String sessionId() {
        return UUID.randomUUID().toString();
    }

}
