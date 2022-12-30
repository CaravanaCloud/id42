package id42.cdk.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping
public interface SlotTypeConfig {
    String name();
    String description();
    String resolutionStrategy();
    List<SlotTypeValueConfig> values();
}
