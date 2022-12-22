package id42.chat;

import org.telegram.telegrambots.meta.api.objects.Message;

public record Input(String command,
                    String prompt) {

    public static Input of(Message msg) {
        var text = msg.getText();
        return of(text);
    }

    public static Input of(String text) {
        if (text == null) return null;
        var rootSize = text.indexOf(' ');
        if (rootSize == -1) rootSize = text.length();
        var root = text.substring(0, rootSize);
        var prompt = text.substring(rootSize);
        var rootTokens = root.split("@");
        var command = rootTokens[0];
        return new Input(command, prompt);
    }
}
