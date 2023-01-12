package id42.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatLocale {
    private final String code;
    private final String voice;
    private final Number nluConfidenceThreshold;

    public List<ChatIntent> intents = new ArrayList<>();
    private final List<ChatSlotType> slotTypes = new ArrayList<>();


    private ChatLocale(String code, double nluConfidenceThreshold, String voice) {
        this.code = code;
        this.nluConfidenceThreshold = nluConfidenceThreshold;
        this.voice = voice;
    }

    public static ChatLocale of(String code, double nluConfidenceThreshold, String voice) {
        return new ChatLocale(code, nluConfidenceThreshold, voice);
    }

    public String code() {
        return code;
    }

    public String localeId() {
        return code;
    }

    public Number nluConfidenceThreshold() {
        return nluConfidenceThreshold;
    }

    public List<ChatIntent> intents() {
        return intents;
    }

    public String voice() {
        return voice;
    }

    public boolean addIntent(ChatIntent intent) {
        return intents.add(intent);
    }

    public List<ChatSlotType> slotTypes() {
        return slotTypes;
    }
}
