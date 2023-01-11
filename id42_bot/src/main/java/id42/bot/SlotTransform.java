package id42.bot;

import java.util.Map;

public record SlotTransform(String outputText,
                            Map<String, String> slots) {
    public static SlotTransform of(String result, Map<String, String> slots) {
        return new SlotTransform(result, slots);
    }
}
