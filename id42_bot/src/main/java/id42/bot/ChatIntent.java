package id42.chat.bot;

import java.util.Map;

public record ChatIntent(
        String name,

        State state,
        String message,
        Map<String, String> slots) {
    static final ChatIntent EMPTY = new ChatIntent("", State.EMPTY, "", Map.of());
    public static ChatIntent empty() {
        return EMPTY;
    }


    public enum State {
        READY,
        PARTIAL,
        FAIL,
        EMPTY
    }
    public static ChatIntent fail(String message) {
        return new ChatIntent("fail", State.FAIL, message, Map.of());
    }
    public static ChatIntent ready(String name, String message, Map<String, String> slots) {
        return new ChatIntent(name, State.READY, message, slots);
    }

    public static ChatIntent partial(String name, String message, Map<String, String> slots) {
        return new ChatIntent(name, State.PARTIAL, message, slots);
    }

    public ChatIntent withSlots(Map<String, String> merged) {
        return new ChatIntent(name, state, message, merged);
    }

}
