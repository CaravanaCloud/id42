package id42.chat;

import id42.intent.ID42Intents;
import id42.intent.ID42Slots;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatInteraction {
    ChatInteractionState state;
    IntentKey intent;
    String locale;
    String text;
    Map<SlotKey, Object> slots;

    public ChatInteraction(String outputText,
                           IntentKey intent,
                           Map<SlotKey, Object> slots,
                           ChatInteractionState state) {
        this.slots = slots;
        this.text = outputText;
        this.intent = intent;
        this.state = state;
    }

    public static ChatInteraction of(
            String outputText,
            IntentKey intent,
            Map<SlotKey, Object> slots,
            ChatInteractionState state){
        return new ChatInteraction(outputText, intent, slots, state);
    }

    public static ChatInteraction empty() {
        return new ChatInteraction("", null, Map.of(), ChatInteractionState.EMPTY);
    }

    public static ChatInteraction fail(String text) {
        return of(text, null, Map.of(), ChatInteractionState.FAIL);
    }

    public static ChatInteraction ready(String intentName,
                                        String responseStr,
                                        HashMap<String, String> slotsOut) {
        return of(responseStr, intentByName(intentName), slotsByName(slotsOut), ChatInteractionState.READY);
    }

    private static Map<SlotKey, Object> slotsByName(HashMap<String, String> slots) {
        if (slots == null) return Map.of();
        return slots.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> ID42Slots.valueOf(e.getKey()),
                        e -> e.getValue()));
    }

    private static IntentKey intentByName(String intentName) {
        return ID42Intents.byName(intentName);
    }

    public static ChatInteraction partial(String intentName, String responseStr, HashMap<String, String> slotsOut) {
        return of(responseStr, intentByName(intentName), slotsByName(slotsOut), ChatInteractionState.PARTIAL);
    }


    public IntentKey intent() {
        return intent;
    }

    public Map<SlotKey, Object> slots() {
        return slots;
    }

    public <T> List<T> slotList(Class<T> type, SlotKey key) {
        var object = slot(key);
        if(object == null)
            return List.of();
        if(object instanceof List)
            return (List<T>) object;
        throw new IllegalArgumentException("Slot " + key + " is not a list");
    }

    private Object slot(SlotKey key) {
        return slots.get(key);
    }

    public String outputText() {
        return text;
    }

    public ChatInteraction withSlots(HashMap<SlotKey, Object> merged) {
        this.slots = merged;
        return this;
    }

    public ChatInteractionState state() {
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
}
