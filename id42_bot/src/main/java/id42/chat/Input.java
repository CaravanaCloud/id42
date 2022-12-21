package id42.chat;

import org.telegram.telegrambots.meta.api.objects.Message;

public record Input(String command,
                    String[] prompt) {

    public static Input of(Message msg) {
        var text = msg.getText();
        return of(text);
    }

    public static Input of(String text) {
        if (text == null) return null;
        var textTokens = text.split("@");
        var command = textTokens[0];
        var prompt = new String[]{};
        if (textTokens.length > 1) {
            prompt = new String[textTokens.length - 1];
            System.arraycopy(textTokens, 1, prompt, 0, prompt.length);
        }
        return new Input(command, prompt);
    }
}
