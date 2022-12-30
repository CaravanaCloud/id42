package id42.chat.bot;

import id42.Identity;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.UUID;

public record Input(String sessionId,
                    String command,
                    Prompt prompt) {

    public static Input of(Message msg) {
        var text = msg.getText();
        var sessionId = msg.getChatId().toString();
        return of(sessionId, text);
    }

    public static Input of(String text) {
        return of(UUID.randomUUID().toString(), text);
    }

    public static Input of(String sessionId, String text) {
        if (text == null) return null;
        String prompt = null;
        String command = null;
        var tokens = text.split(" ");
        if (tokens.length > 0) {
            var head = tokens[0];
            if (head.startsWith("/")){
                var atPos = head.indexOf("@");
                if (atPos == -1) atPos = head.length();
                command = head.substring(0, atPos);
                var prompts = Arrays.copyOfRange(tokens, 1, tokens.length);
                prompt = String.join(" ", prompts);
            }else {
                var prompts = Arrays.copyOfRange(tokens, 0, tokens.length);
                prompt = String.join(" ", prompts);
            }
        }
        var inputPrompt = Prompt.of(prompt);
        return new Input(sessionId, command, inputPrompt);
    }


    public static Input of(Identity identity, String message) {
        //TODO: Add identity field / consider replacing sessioon id with session identity
        return of(message);
    }

    @Override
    public String command() {
        return command.toLowerCase();
    }
}
