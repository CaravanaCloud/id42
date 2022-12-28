package id42.chat;

public class ChatSlot {

    private final String name;
    private final String type;
    private final String descripton;
    private ChatSlotElicitation elicitation;


    public ChatSlot(String name, String description, String type) {
        this.name = name;
        this.type = type;
        this.descripton = description;
    }


    public static ChatSlot of(String name,
                              String description,
                              String type,
                              String slotConstraint,
                              String message,
                              Integer maxRetries) {
        var slot = new ChatSlot(name, description, type);
        var elicitation = ChatSlotElicitation.of(slotConstraint, message, maxRetries);
        slot.elicitation(elicitation);
        return slot;
    }

    public void elicitation(ChatSlotElicitation elicitation) {
        this.elicitation = elicitation;
    }

    public String name() {
        return name;
    }

    public String description() {
        return descripton;
    }

    public String type() {
        return type;
    }

    public ChatSlotElicitation elicitation() {
        return elicitation;
    }
}
