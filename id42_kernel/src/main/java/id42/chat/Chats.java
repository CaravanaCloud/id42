package id42.chat;


import java.util.ArrayList;
import java.util.List;

public class Chats {

    private static final String AMAZON_ALPHANUMERIC = "AMAZON.AlphaNumeric";
    private static final String REQUIRED = "Required";
    private static final Integer DEFAULT_RETRIES = 3;

    private final List<ChatLocale> locales = new ArrayList<>();



    public ChatSlot requiredSlot(String name, String description, String type, String message) {
        return slot(name, description, type, REQUIRED, message, DEFAULT_RETRIES);
    }

    public ChatSlot requiredSlot(String name, String description, String message) {
        return slot(name, description, AMAZON_ALPHANUMERIC, REQUIRED, message, DEFAULT_RETRIES);
    }


    public ChatSlot slot(String name, String description, String type, String constraint, String message, Integer retries) {
        return ChatSlot.of(name, description, type, constraint, message, retries);
    }


    public List<ChatLocale> locales() {
        return locales;
    }
}
