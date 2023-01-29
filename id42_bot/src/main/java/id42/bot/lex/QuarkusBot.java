package id42.bot.lex;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.inject.Inject;

public class QuarkusBot implements QuarkusApplication {
    private static final String VERSION= "1.9";
    @Inject
    Listener listener;

    @Inject
    BotConfig config;

    @Inject
    Logger log;
    @Override
    public int run(String... args) throws Exception {
        log.info("Starting id42 bot ");
        log.debug("version: {}", VERSION);
        log.info("Telegram user: {}", config.username().orElse("?"));
        log.info("Lex id/alias: {}/{}",
                config.lexBotId(),
                config.lexBotAliasId());
        var botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(listener);
        log.info("Listener registered, waiting for exit.");
        Quarkus.waitForExit();
        log.info("Quarkus exited.");
        return 0;
    }
}
