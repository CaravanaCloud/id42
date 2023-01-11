package id42.bot;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class SlotOverrides {
    List<SlotOverride> overrides = SlotOverridesES.overrides();

    //TODO: Also return captured slot values after transform
    public SlotTransform transform(String text) {
        var slots = new HashMap<String, String>();
        for(SlotOverride override: overrides) {
            var transform = override.transform(text, slots);
            text = transform.outputText();
            slots.putAll(transform.slots());
        }
        return SlotTransform.of(text, slots);
    }
}
