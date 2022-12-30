package id42.chat.bot;

import java.util.Arrays;

public record Prompt(String text,
                     String action,
                     String object,
                     String property,
                     String arguments) {

    public static Prompt of(String text) {
        if (text == null) return null;
        String action = null;
        String property = null;
        String object = null;
        String arguments = null;
        if (text.startsWith(HAL.WAKEWORD)){
            var tokens = text.split(" ");
            if (tokens.length > 0)
                action = tokens[0].replaceFirst(HAL.WAKEWORD, "");
            if (tokens.length > 1)
                object = tokens[1];
            if (tokens.length > 2)
                property = tokens[2];
            if (tokens.length > 3)
                arguments = String.join(" ", Arrays.copyOfRange(tokens, 3, tokens.length));
        }
        return new Prompt(text, action, object, property, arguments);
    }
}
