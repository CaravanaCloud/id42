package id42.chat;

public record ChatValueSelectionSetting(String resolutionStrategy) {
    public static ChatValueSelectionSetting of(String resolutionStrategy) {
        var chatValueSelectionSetting = new ChatValueSelectionSetting(resolutionStrategy);
        return chatValueSelectionSetting;
    }
}
