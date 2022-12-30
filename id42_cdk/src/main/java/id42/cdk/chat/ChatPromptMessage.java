package id42.cdk.chat;

public class ChatPromptMessage {
    private String message;

    public ChatPromptMessage(String message) {
        this.message = message;
    }

    public static ChatPromptMessage of(String message) {
        var chatMessage = new ChatPromptMessage(message);
        return chatMessage;
    }

    public String message() {
        return message;
    }
}
