package id42.chat.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlotOverridesES {
    static final Map<String, String> clocks = new HashMap<>(){{
        put("🕙", "10:00");
        put("🕚", "11:00");
        put("🕛", "12:00");
        put("🕐", "13:00");
        put("🕑", "14:00");
        put("🕒", "15:00");
        put("🕓", "16:00");
        put("🕔", "17:00");
        put("🕕", "18:00");
        put("🕖", "19:00");
        put("🕗", "20:00");
        put("🕘", "21:00");
    }};

    static final List<SlotOverride> overrides = List.of(
            SlotOverride.of("pickupContact",
                    "[Cc]liente:[\\s+](.*)"),
            SlotOverride.of("pickupTime",
                    "[Hh]ora:[\\s+](\\S*)",
                    SlotOverridesES::parseTime),
            SlotOverride.of("pickupTime",
                    "(🕐|🕑|🕒|🕓|🕔|🕕|🕖|🕗|🕘|🕙|🕚|🕛)",
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
                    "(🕐|🕑|🕒|🕓|🕔|🕕|🕖|🕗|🕘|🕙|🕚|🕛)",
            h -> switch(h){
                case "🕛"  -> "12:00";
                default -> h;
            });
            var text = "🕛 de la noche en Habana, Cuba";
            var out = slot.transform(text, new HashMap<>());
            System.out.println(out.outputText());
    }
}