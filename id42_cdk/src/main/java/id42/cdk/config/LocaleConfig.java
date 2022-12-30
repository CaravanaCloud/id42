package id42.cdk.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@SuppressWarnings("unused")
@ConfigMapping
public interface LocaleConfig {
    String code();
    List<SlotTypeConfig> slotTypes();
    List<IntentConfig> intentConfig();
}
