package id42.chat;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "bot")
public interface BotConfig {
    String username();
    String token();
}
