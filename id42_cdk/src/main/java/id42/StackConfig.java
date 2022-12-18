package id42;

import java.util.List;
import java.util.Optional;

public enum StackConfig {
    certificateARN("arn:aws:acm:us-east-1:746658282231:certificate/cf889a63-7b97-46e8-9c22-e790d064769d"),
    domainNames("*.id42.cc,id42.cc"),
    botUsername("id42_dev_bot"),
    botToken(null);
    private static final String PREFIX = "ID42_";

    private final String defaultValue;

    StackConfig(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getString() {
        var value = Optional.of(PREFIX + name().toUpperCase())
                .map(System::getenv)
                .orElse(defaultValue);
        return value;
    }

    public List<String> getList() {
        return List.of(getString().split(","));
    }
}
