package id42.chat.bot;

import java.util.HashMap;
import java.util.Map;

public record SlotTransform(String outputText,
                            Map<String, String> slots) {
    public static SlotTransform of(String result, Map<String, String> slots) {
        return new SlotTransform(result, slots);
    }
}
