package id42.chat.bot;

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

    public String transform(String text) {
        var matcher = pattern.matcher(text);
        var found = matcher.find();
        if(found) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i
                        + ": "
                        + matcher.group(i)
                        + " (start: "
                        + matcher.start(i)
                        + ", end: "
                        + matcher.end(i)
                        + ")");
            }
            var value = matcher.group(1);
            var start = matcher.start(1);
            var end = matcher.end(1);
            var newValue = transform == null ? value : transform.apply(value);
            var prefix = text.substring(0, start);
            var skip = value.length();
            var suffix = text.substring(start+skip, text.length());
            var result = prefix + newValue + suffix;
            return result;
        }
        return text;
    }
}
