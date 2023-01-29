package id42.intent.es;

import id42.chat.ChatLocale;
import id42.chat.ChatSlotType;
import id42.chat.ChatValueSelectionSetting;

import java.util.List;

public class SlotTypesES {

    public static void of(ChatLocale locale) {
        var contactSlotType = ChatSlotType.of("ID42_CONTACT",
                "Contact",
                ChatValueSelectionSetting.of("ORIGINAL_VALUE"),
                ContactSlotTypeES.values());

        var locationSlotType = ChatSlotType.of("ID42_LOCATION",
                "Address or place name",
                ChatValueSelectionSetting.of("ORIGINAL_VALUE"),
                LocationSlotTypeES.values());

        var weekdaySlotType = ChatSlotType.of("ID42_WEEKDAY",
                "Day of the week",
                ChatValueSelectionSetting.of("TOP_RESOLUTION"),
                List.of("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"));

        var slotTypes = List.of(locationSlotType,
                contactSlotType,
                weekdaySlotType);
        locale.slotTypes().addAll(slotTypes);
    }
}
