package id42.chat.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlotOverridesES {
    static final Map<String, String> clocks = new HashMap<>(){{
        put("🕐", "01:00");
        put("🕑", "02:00");
        put("🕒", "03:00");
        put("🕓", "04:00");
        put("🕔", "05:00");
        put("🕕", "06:00");
        put("🕖", "07:00");
        put("🕗", "08:00");
        put("🕘", "09:00");
        put("🕙", "10:00");
        put("🕚", "11:00");
        put("🕛", "12:00");
    }};

    static final List<SlotOverride> overrides = List.of(
            SlotOverride.of("pickupContact",
                    "[Cc]liente:[\\s?](.*)"),
            SlotOverride.of("pickupTime",
                    "[Hh]ora:[\\s?](\\S*)",
                    SlotOverridesES::parseTime)
            );

    private static String parseTime(String time) {
        return clocks.getOrDefault(time, time);
    }

    public static List<SlotOverride> overrides() {
        return overrides;
    }

    public static void main(String[] args) {
            var slot = SlotOverride.of("pickupTime",
            "[Hh]ora:[\\s?](\\S*)",
            h -> switch(h){
                case "\uD83D\uDD5B"  -> "12:00";
                default -> h;
            });
            var text = "la hora: \uD83D\uDD5B en la noche";
            var out = slot.transform(text);
            System.out.println(out);
    }
}