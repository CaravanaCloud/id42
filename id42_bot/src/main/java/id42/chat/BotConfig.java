package id42.chat;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "bot")
public interface BotConfig {
    String username();
    String token();

    List<IntentConfig> intents();
}
