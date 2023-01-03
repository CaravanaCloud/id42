package id42.chat;

import id42.Identity;
import id42.chat.bot.Input;
import id42.chat.bot.Listener;
import id42.chat.bot.Outcome;

import javax.inject.Inject;
import java.util.UUID;

public abstract class ChatTest {
    @Inject
    Listener listener;
    protected Identity identity() {
        return TestIdentity.anonymous();
    }


    protected Outcome ask(String prompt) {
        return listener.ingest(identity(),
                sessionId(),
                "/ask "+prompt);
    }

    protected String sessionId() {
        return UUID.randomUUID().toString();
    }

}
