package id42.cdk.config;

import io.smallrye.config.ConfigMapping;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "chat")
public interface ChatConfig {
    Optional<String> name();
    List<LocaleConfig> locales();
}
