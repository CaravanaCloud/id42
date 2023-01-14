package id42.intent;

import id42.chat.SlotKey;

import java.util.List;

public class ID42Slots implements SlotKey {
    public static List<SlotKey> allSlots() {
        return List.of(
                DeliverySlot.values()
        );
    }

    //TODO: Replace lookup with a map
    public static SlotKey valueOf(String key) {
        var slot = allSlots().stream()
                .filter(s -> s.toString().equals(key))
                .findFirst();
        if (slot.isEmpty()){
            warn("No slot with key " + key);
        }
        return slot.orElse(null);
    }

    private static void warn(String s) {
        System.out.println();
    }
}
