package id42.chat.bot;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SlotOverrides {
    List<SlotOverride> overrides = SlotOverridesES.overrides();
    public String transform(String text) {
        for(SlotOverride override: overrides) {
            text = override.transform(text);
        }
        return text;
    }
}
