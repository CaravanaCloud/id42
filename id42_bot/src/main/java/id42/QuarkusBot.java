package id42;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.inject.Inject;

public class QuarkusBot implements QuarkusApplication {
    @Inject
    Listener listener;

    @Override
    public int run(String... args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(listener);
        Quarkus.waitForExit();
        return 0;
    }
}
