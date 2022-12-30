package id42.chat.bot;

import java.util.HashMap;
import java.util.Map;

public record Outcome(Outcome.Type type,
                      String message,
                      Map<String, String> slots) {
    static final Outcome EMPTY_OUT = new Outcome(Type.EMPTY, "", Map.of());
    public static Outcome empty() {
        return EMPTY_OUT;
    }

    public enum Type {
        READY,
        PARTIAL,
        FAIL,

        EMPTY
    }
    public static Outcome fail(String s) {
        return new Outcome(Type.FAIL, s, Map.of());
    }
    public static Outcome ready(String s, Map<String, String> slots) {
        return new Outcome(Type.READY, s, slots);
    }

    public static Outcome partial(String message, Map<String, String> slots) {
        return new Outcome(Type.PARTIAL, message, slots);
    }

}
