package id42.chat.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlotOverride{

    private final String slotName;
    private final String slotPattern;
    private final Pattern pattern;
    private final Function<String, String> transform;

    private SlotOverride(String slotName,
                         String slotPattern,
                         Function<String, String> transform){
        this.slotName = slotName;
        this.slotPattern = slotPattern;
        this.pattern = Pattern.compile(slotPattern);
        this.transform = transform;
    }

    public static SlotOverride of(String slotName,
                                  String slotPattern){
        return new SlotOverride(slotName, slotPattern, null);
    }

    public static SlotOverride of(String slotName,
                            String slotPattern,
                                  Function<String, String> transform){
        return new SlotOverride(slotName, slotPattern, transform);
    }

    public SlotTransform transform(String text, Map<String, String> slots){
        var newSlots = new HashMap<String, String>();
        var matcher = pattern.matcher(text);
        var found = matcher.find();
        if(found && matcher.groupCount() > 0) {
            var value = matcher.group(1);
            var start = matcher.start(1);
            var end = matcher.end(1);
            var newValue = transform == null ?
                    value :
                    transform.apply(value);
            var prefix = text.substring(0, start);
            var skip = value.length();
            var suffix = text.substring(start+skip);
            var result = prefix + newValue + suffix;
            return SlotTransform.of(result, Map.of(slotName, newValue));
        }
        return SlotTransform.of(text, Map.of());
    }
}
