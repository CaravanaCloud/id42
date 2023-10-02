package id42.chat;

public class ChatSlotPriority {
    String slotId;
    Integer priority;

    public ChatSlotPriority(String slotId, int priority) {
        this.slotId = slotId;
        this.priority = priority;
    }

    public static ChatSlotPriority of(String slotId, int priority) {
        var chatSlotPriority = new ChatSlotPriority(slotId, priority);
        return chatSlotPriority;
    }

    public String slotId() {
        return slotId;
    }

    public Number priority() {
        return priority;
    }
}
