package id42.chat;

public class ChatUtterance {

    private String value;

    public ChatUtterance(String utterance) {
        this.value = utterance;
    }

    public static void of(ChatIntent intent, String... utterances) {
        for (String utterance : utterances) {
            var chatUtterance = new ChatUtterance(utterance);
            intent.utterances().add(chatUtterance);
        }
    }

    public String value() {
        return this.value;
    }
}
