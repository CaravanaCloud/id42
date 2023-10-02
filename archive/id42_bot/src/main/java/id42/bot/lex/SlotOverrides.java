package id42.bot.lex;

import id42.chat.ChatRequest;
import id42.chat.ChatRequestState;
import id42.chat.IntentKey;
import id42.chat.SlotKey;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class SlotOverrides {
    List<SlotOverride> overrides = SlotOverridesES.overrides();

    //TODO: Also return captured slot values after transform
    public ChatRequest transform(String text) {
        var slots = new HashMap<SlotKey, Object>();
        var intent = (IntentKey) null;
        var state = (ChatRequestState) null;
        var sessionId = (String) null;
        for(SlotOverride override: overrides) {
            var transform = override.transform(text, slots);
            text = transform.outputText();
            slots.putAll(transform.slots());
            intent = transform.intent();
            state = transform.state();
        }
        return ChatRequest.of(text, intent, slots, state, sessionId);
    }
}
