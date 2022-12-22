package id42.chat.intent;

import id42.chat.BotConfig;
import id42.chat.Input;
import id42.chat.IntentConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Intents {
    @Inject
    BotConfig config;

    public Optional<IntentConfig> of(Input input){
        var text = input.prompt();
        var intent = lookupIntent(text);
        return intent;
    }

    private Optional<IntentConfig> lookupIntent(String text) {
        var intentConfigs = config.intents();
        var result = intentConfigs
                .stream()
                .filter(intent -> this.matchKeywords(text, intent.keywords()))
                .findFirst();
        return result;
    }

    private boolean matchKeywords(String prompt, List<String> keywords) {
        var lowPrompt = prompt.toLowerCase();
        var match = keywords.stream()
                .map(x -> x.toLowerCase())
                .anyMatch(lowPrompt::contains);
        return match;
    }
}
