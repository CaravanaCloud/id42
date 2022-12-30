package id42.cdk.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatPrompt {
    List<ChatPromptMessageGroup> messageGroupsList = new ArrayList<>();
    Integer maxRetries;

    public ChatPrompt(List<ChatPromptMessageGroup> messageGroupList, Integer maxRetries) {
        this.messageGroupsList = messageGroupList;
        this.maxRetries = maxRetries;
    }

    public static ChatPrompt of(String message,
                                Integer maxRetries) {
        var chatMessage = ChatPromptMessage.of(message);
        var messageGroup = ChatPromptMessageGroup.of(chatMessage);
        var messageGroupList = List.of(messageGroup);
        var chatPrompt = new ChatPrompt(messageGroupList, maxRetries);
        return chatPrompt;
    }

    public Number maxRetries() {
        return maxRetries;
    }

    public List<ChatPromptMessageGroup> messageGroups() {
        return messageGroupsList;
    }
}
