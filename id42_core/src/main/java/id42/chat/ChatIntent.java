package id42.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatIntent{
    String name;
    List<ChatSlot> slots;
    List<ChatUtterance> utterances;
    List<ChatSlotPriority> slotPriorities;

    public ChatIntent( String name,
    List<ChatSlot> slots,
    List<ChatUtterance> utterances,
    List<ChatSlotPriority> slotPriorities) {
        this.name = name;
        this.slots = slots;
        this.utterances = utterances;
        this.slotPriorities = slotPriorities;
    }

    public static ChatIntent of(String intentName, ChatLocale chatLocale) {
        var intent = of(intentName);
        chatLocale.addIntent(intent);
        return intent;
    }

    private static ChatIntent of(String intentName) {
        return new ChatIntent(intentName,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }

    public String description() {
        return name;
    }

    public void slots(List<ChatSlot> slots) {
        //TODO: pass to constructor instead
        this.slots.addAll(slots);
    }

    public void slotPriorities(List<ChatSlotPriority> slotPriorities) {
        //TODO: pass to constructor instead
        this.slotPriorities.addAll(slotPriorities);
    }

    public List<ChatUtterance> utterances() {
        return utterances;
    }

    public String name() {
        return name;
    }

    public List<ChatSlot> slots() {
        return slots;
    }

    public List<ChatSlotPriority> slotPriorities() {
        return slotPriorities;
    }
}
