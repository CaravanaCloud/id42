package id42.chat;

import id42.intent.ID42Intents;
import id42.intent.ID42Slots;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatRequest {
    ChatRequestState state;
    IntentKey intent;
    String locale;
    String text;
    Map<SlotKey, Object> slots;

    String sessionId;

    public ChatRequest(String outputText,
                       IntentKey intent,
                       Map<SlotKey, Object> slots,
                       ChatRequestState state,
                       String sessionId) {
        this.slots = slots;
        this.text = outputText;
        this.intent = intent;
        this.state = state;
        this.sessionId = sessionId;
    }

    public static ChatRequest of(
            String outputText,
            IntentKey intent,
            Map<SlotKey, Object> slots,
            ChatRequestState state,
            String sessionId) {
        return new ChatRequest(outputText, intent, slots, state, sessionId);
    }

    public static ChatRequest empty() {
        return new ChatRequest("",
                null,
                Map.of(),
                ChatRequestState.EMPTY,
                null);
    }

    public static ChatRequest fail(String text) {
        return of(text, null, Map.of(), ChatRequestState.FAIL, null);
    }

    public static ChatRequest ready(String intentName,
                                    String responseStr,
                                    Map<String, String> slotsOut,
                                    String sessionId) {
        return of(responseStr, intentByName(intentName), slotsByName(slotsOut), ChatRequestState.READY, sessionId);
    }

    private static Map<SlotKey, Object> slotsByName(Map<String, String> slots) {
        if (slots == null) return Map.of();
        var slotsMap = slots.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> ID42Slots.valueOf(e.getKey()),
                        e -> (Object) e.getValue()));
        return slotsMap;
    }

    private static IntentKey intentByName(String intentName) {
        return ID42Intents.byName(intentName);
    }

    public static ChatRequest partial(String intentName, String responseStr, HashMap<String, String> slotsOut, String sessionId) {
        return of(responseStr, intentByName(intentName), slotsByName(slotsOut), ChatRequestState.PARTIAL, sessionId);
    }

    public static ChatRequest of(IntentKey intent) {
        return of("", intent, map(), ChatRequestState.EMPTY, null);
    }

    private static Map<SlotKey, Object> map() {
        return new HashMap<>();
    }


    public IntentKey intent() {
        return intent;
    }

    public Map<SlotKey, Object> slots() {
        return slots;
    }

    public <T> List<T> slotList(Class<T> type, SlotKey key) {
        var object = slot(key);
        if (object == null)
            return List.of();
        if (object instanceof List)
            return (List<T>) object;
        throw new IllegalArgumentException("Slot " + key + " is not a list");
    }

    private Object slot(SlotKey key) {
        return slots.get(key);
    }

    public String outputText() {
        return text;
    }

    public ChatRequest withSlots(HashMap<SlotKey, Object> merged) {
        this.slots = merged;
        return this;
    }

    public ChatRequestState state() {
        return state;
    }

    public String text() {
        return text;
    }

    public String getString(SlotKey slot) {
        var value = slot(slot);
        if (value == null)
            return null;
        if (value instanceof String)
            return (String) value;
        throw new IllegalArgumentException("Slot " + slot + " is not a string");
    }

    public String sessionId() {
        return this.sessionId;
    }

    public ChatRequest put(SlotKey slotKey, String value) {
        this.slots.put(slotKey, value);
        return this;
    }

    public <T> T get(SlotKey key, Class<T> type) {
        var val = slots.get(key);
        if (! type.isInstance(val)){
            throw new IllegalArgumentException("Slot " + key + " is not a " + type.getName());
        }
        return (T) val;
    }
}
