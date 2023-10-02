package id42.bot.lex;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping
public interface IntentConfig {
    String name();
    List<String> keywords();
}
