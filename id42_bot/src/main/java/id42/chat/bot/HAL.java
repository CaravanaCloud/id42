package id42.chat.bot;


import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;


@ApplicationScoped
public class HAL {
    public static final String WAKEWORD = ".";

    @Inject
    Logger log;

    public String ask(String prompt) {
        var input = Input.of(UUID.randomUUID().toString(),
                prompt);
        var out = ask(input);
        return out.message();
    }

    public Outcome ask(Input input) {
        if (input == null) return Outcome.fail("No input...");
        var prompt = input.prompt();
        return intent(prompt, prompt.action());
    }

    private Outcome intent(Prompt prompt, String action) {
        if(action == null) return Outcome.fail("No action...");
        return switch (action){
            case "set" -> set(prompt);
            default -> Outcome.fail("Unknown action: " + action);
        };
    }

    //TODO: Resolve to service
    private Outcome set(Prompt prompt) {
        var object = prompt.object();
        var property = prompt.property();
        var args = prompt.arguments();
        var message = "[%s].[%s] := [%s]".formatted(object, property, args);
        return Outcome.ready(message, Map.of());
    }


}
