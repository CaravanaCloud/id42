package id42.bot;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import software.amazon.awssdk.regions.Region;

import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "bot")
public interface BotConfig {
    Optional<String> username();
    Optional<String> token();


    @WithDefault("us-west-2")
    String regionName();

    default Region region() {
        return Region.of(regionName());
    }

    String lexBotAliasId();

    String lexBotId();

    @WithDefault("false")
    boolean debugMode();

    //TODO: Deprecate
    List<IntentConfig> intents();
}
