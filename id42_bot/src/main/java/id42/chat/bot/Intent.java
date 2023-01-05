package id42.chat.bot;

import java.util.Map;

public record Intent(
        String name,

        State state,
        String message,
        Map<String, String> slots) {
    static final Intent EMPTY = new Intent("", State.EMPTY, "", Map.of());
    public static Intent empty() {
        return EMPTY;
    }


    public enum State {
        READY,
        PARTIAL,
        FAIL,
        EMPTY
    }
    public static Intent fail(String name, String message) {
        return new Intent(name, State.FAIL, message, Map.of());
    }
    public static Intent ready(String name, String message, Map<String, String> slots) {
        return new Intent(name, State.READY, message, slots);
    }

    public static Intent partial(String name, String message, Map<String, String> slots) {
        return new Intent(name, State.PARTIAL, message, slots);
    }

    public Intent withSlots(Map<String, String> merged) {
        return new Intent(name, state, message, merged);
    }

}
