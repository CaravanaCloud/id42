package id42.lex;

import com.google.common.base.MoreObjects;
import id42.Identity;

import java.util.Arrays;

public class Input {
    private final Identity identity;
    private final String sessionId;
    private final String text;
    String command;
    String prompt;

    public Input(Identity identity,
                 String sessionId,
                 String text) {
        this.identity = identity;
        this.sessionId = sessionId;
        this.text = text;
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
    }

    public static Input of(Identity identity,
                           String sessionId,
                           String text) {
        if (text == null) return null;
        return new Input(identity, sessionId, text);
    }


    public String sessionId() {
        return sessionId;
    }

    public String text() {
        return text;
    }

    public String command() {
        return command != null ? command : "";
    }

    public String prompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("identity", identity)
                .add("sessionId", sessionId)
                .add("text", text)
                .add("_command", command)
                .add("_prompt", prompt)
                .toString();
    }
}
