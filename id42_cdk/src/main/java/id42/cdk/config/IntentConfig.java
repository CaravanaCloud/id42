package id42.cdk.config;


import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping
public interface IntentConfig {
    List<String> utterances();
}
