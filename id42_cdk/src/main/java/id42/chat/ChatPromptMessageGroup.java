package id42.chat;

public class ChatPromptMessageGroup {
    private ChatPromptMessage message;

    public ChatPromptMessageGroup(ChatPromptMessage message) {
        this.message = message;
    }

    public static ChatPromptMessageGroup of(ChatPromptMessage message) {
        return new ChatPromptMessageGroup(message);
    }


    public ChatPromptMessage message() {
        return message;
    }

}
