package id42.cdk;

import java.util.List;
import java.util.Optional;

public enum StackConfig {
    certificateARN("arn:aws:acm:us-east-1:746658282231:certificate/cf889a63-7b97-46e8-9c22-e790d064769d"),
    domainNames("*.id42.cc,id42.cc"),
    bot_username("id42_dev_bot"),
    bot_token(null),

    lex_botid(null),

    lex_botalias(null),
    deployToS3("true"),
    instanceType("t3.nano"),
    db_root_username("admin"),
    db_root_password("Masterkey123"),
    db_name("id42db"),

    nlu_threshold("0.5"),

    bot_max_retries("3");

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

    public boolean getBoolean(){
        return Boolean.parseBoolean(getString());
    }

    public double getDouble(){
        return Double.parseDouble(getString());
    }

    public double getInteger(){
        return Integer.parseInt(getString());
    }
}
