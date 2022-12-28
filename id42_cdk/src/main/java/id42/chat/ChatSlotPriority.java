package id42.chat;

public record ChatSlotPriority(String slotId,
                               Integer priority) {

    public static ChatSlotPriority of(String slotId, int priority) {
        var chatSlotPriority = new ChatSlotPriority(slotId, priority);
        return chatSlotPriority;
    }
}
