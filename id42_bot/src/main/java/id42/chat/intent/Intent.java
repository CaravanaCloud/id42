package id42.chat.intent;

public interface Intent {
    Outcome apply(Slots slots);
}
