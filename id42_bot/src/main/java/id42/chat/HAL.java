package id42.chat;

import org.telegram.telegrambots.meta.api.objects.Message;

import javax.enterprise.context.Dependent;

@Dependent
public class HAL {
    public String ask(String prompt) {
        var input = Input.of(prompt);
        return ask(input);
    }

    public String ask(Input input) {
        return "I'm sorry. I'm afraid I can't do that.";
    }
}
