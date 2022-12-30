package id42.cdk.chat;

public class ChatSlotElicitation {
    private String slotConstraint;
    private ChatPrompt prompt;

    public ChatSlotElicitation(String slotConstraint, ChatPrompt prompt) {
        this.slotConstraint = slotConstraint;
        this.prompt = prompt;
    }

    public static ChatSlotElicitation of(String slotConstraint, String message, Integer maxRetries) {
        var chatPrompt = ChatPrompt.of(message, maxRetries);
        var elicitation = new ChatSlotElicitation(slotConstraint, chatPrompt);
        return elicitation;
    }


    public void prompt(ChatPrompt chatPrompt) {
        this.prompt = prompt;
    }

    public ChatPrompt prompt() {
        return prompt;
    }

    public String slotConstraint() {
        return slotConstraint;
    }
}
