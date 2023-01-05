package id42.chat;

import id42.Identity;
import id42.chat.bot.Listener;
import id42.chat.bot.ChatIntent;

import javax.inject.Inject;
import java.util.UUID;

public abstract class ChatTest {
    @Inject
    Listener listener;
    protected Identity identity() {
        return TestIdentity.anonymous();
    }


    protected ChatIntent ask(String prompt) {
        return listener.ingest(identity(),
                sessionId(),
                "/ask "+prompt);
    }

    protected String sessionId() {
        return UUID.randomUUID().toString();
    }

}
