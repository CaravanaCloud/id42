package id42.chat;

import java.util.List;

//TODO: Map more precise slot types (Day of Week, PartialDate, LocationAlias ...)
//TODO: Add integration
public class ID42Chat {

    public static Chats of(){
            System.out.println("Initializing chat engine");
            var chats = new Chats();
            addEsEs(chats);
            return chats;
    }

    private static ChatLocale addEsEs(Chats chats) {
        var es = ChatLocale.of("es_ES", 0.75, "Lucia");
        addEsEsSlotTypes(chats, es);
        addEsEsIntents(chats, es);
        chats.locales().add(es);
        return es;
    }

    private static void addEsEsSlotTypes(Chats chats, ChatLocale es) {
        var weekdaySlotType = ChatSlotType.of("ID42_WEEKDAY",
                "Day of the week",
                ChatValueSelectionSetting.of("TOP_RESOLUTION"),
                List.of("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"));
        es.slotTypes().add(weekdaySlotType);

        var locationSlotType = ChatSlotType.of("ID42_LOCATION",
                "Address or place name",
                ChatValueSelectionSetting.of("ORIGINAL_VALUE"),
                List.of("Muntaner 321",
                        "Arco del Triunfo",
                        "Napols 630, esc der, 1a",
                        "Bar Celona",
                        "Provença con Passeig de Gracia",
                        "Carrer de la Diputació, 280",
                        "Carrer de la Diputació, 280, 1a",
                        "Avendia Diagonal, 630"));
        es.slotTypes().add(locationSlotType);
    }

    private static void addEsEsIntents(Chats chats, ChatLocale es) {
        // Slots

        var pickupDateSlot = chats.requiredSlot("pickupDate",
                "Fecha de recogida",
                "AMAZON.Date",
                "¿Que dia lo recogemos?");

        var pickupTimeSlot = chats.requiredSlot("pickupTime",
                "Hora de recogida",
                "AMAZON.Time",
                "¿A que horas recogemos?");

        var pickupLocationSlot = chats.requiredSlot("pickupLocation",
                "Local de recogida",
                "ID42_LOCATION",
                "¿Dónde recogemos?");

        var dropLocationSlot = chats.requiredSlot("dropLocation",
                "Local de entrega",
                "ID42_LOCATION",
                "¿Dónde entregamos?");

        var pickupContactSlot = chats.slot("pickupContact",
                "Contacto para recoger el pedido",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Con quien recogemos?",
                0);

        var weekdaySlot = chats.slot("weekDay",
                "Dia de la semana",
                "ID42_WEEKDAY",
                "Optional",
                "¿Que dia da semana seria?",
                0);

        // Request Delivery Intent - Slots
        var requestDeliveryES = ChatIntent.of("RequestDeliveryES", es);
        var requestDeliveryESSlots = List.of(
                pickupDateSlot,
                pickupTimeSlot,
                pickupLocationSlot,
                pickupContactSlot,
                weekdaySlot);

        requestDeliveryES.slots(requestDeliveryESSlots);
        var slotPriorities = List.of(
                ChatSlotPriority.of("pickupDate",10),
                ChatSlotPriority.of("pickupTime",10),
                ChatSlotPriority.of("pickupLocation",10),
                ChatSlotPriority.of("dropLocation",10),
                ChatSlotPriority.of("pickupContact",10),
                ChatSlotPriority.of("weekDay",10)
        );
        requestDeliveryES.slotPriorities(slotPriorities);
        // Request Delivery Intent - Utterances
        ChatUtterance.of(requestDeliveryES,
                "Programa una entrega",
                "Programar entrega",
                "Necesito una entrega",
                "Necesito un recogido",
                "Programa entrega para {pickupDate} {pickupTime} recoger con {pickupContact} a {pickupLocation}",
         "Programa retirada el {weekDay} {pickupDate} a las {pickupTime} recoger con {pickupContact} en {pickupLocation}",
         "Necesito de una entrega para {weekDay} {pickupDate} a las {pickupTime} en {pickupLocation}"
        );

        // Check Delivery Intent
    }

    public static void main(String[] args) {
            var chats = of();
            System.out.println(chats);
        }
}
