package id42.chat;

public class ChatValueSelectionSetting {
    String resolutionStrategy;

    public ChatValueSelectionSetting(String resolutionStrategy) {
        this.resolutionStrategy = resolutionStrategy;
    }

    public static ChatValueSelectionSetting of(String resolutionStrategy) {
        var chatValueSelectionSetting = new ChatValueSelectionSetting(resolutionStrategy);
        return chatValueSelectionSetting;
    }

    public String resolutionStrategy() {
        return resolutionStrategy;
    }
}
