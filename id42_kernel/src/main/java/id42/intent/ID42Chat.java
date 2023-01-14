package id42.intent;

import id42.chat.*;
import id42.intent.es.RequestDeliveryESUtterances;
import id42.intent.es.SlotTypesES;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        var slotTypes = SlotTypesES.of();
        es.slotTypes().addAll(slotTypes);
        addEsEsIntents(chats, es);
        chats.locales().add(es);
        return es;
    }
    private static void addEsEsIntents(Chats chats, ChatLocale es) {
        // Slots
        var pickDateSlot = chats.requiredSlot("pickDate",
                "Fecha de recogida",
                "AMAZON.Date",
                "¿Que dia lo recogemos?");

        var pickTimeSlot = chats.requiredSlot("pickTime",
                "Hora de recogida",
                "AMAZON.Time",
                "¿A que horas recogemos?");

        var pickLocationSlot = chats.requiredSlot("pickLocation",
                "Local de recogida",
                "ID42_LOCATION",
                "¿Dónde recogemos?");

        var dropLocationSlot = chats.requiredSlot("dropLocation",
                "Local de entrega",
                "ID42_LOCATION",
                "¿Dónde entregamos?");

        var pickContactSlot = chats.slot("pickContact",
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

        var deliveryNoteSlot = chats.slot("deliveryNote",
                "Comentarios para el rider",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Algo más?",
                1);

        var slotsMap = Map.of(
                "pickTime", pickTimeSlot,
                "pickDate", pickDateSlot,
                "pickLocation", pickLocationSlot,
                "pickContact", pickContactSlot,
                "dropLocation", dropLocationSlot,
                "weekDay", weekdaySlot,
                "deliveryNote", deliveryNoteSlot
        );

        var requestDeliveryESSlots = List.of(
                pickDateSlot,
                pickTimeSlot,
                pickLocationSlot,
                pickContactSlot,
                weekdaySlot,
                deliveryNoteSlot,
                dropLocationSlot);

        addRequestDeliveryESIntent(es, slotsMap);
        // Check Delivery Intent
    }

    private static void addRequestDeliveryESIntent(ChatLocale es, Map<String, ChatSlot> slotsMap) {
        // Request Delivery Intent - Slots
        var requestDeliveryES = ChatIntent.of("RequestDeliveryES", es);
        requestDeliveryES.slots(slotsMap.values()
                .stream()
                .collect(Collectors.toList()));
        var slotPriorities = List.of(
                ChatSlotPriority.of("pickDate",20),
                ChatSlotPriority.of("pickTime",20),
                ChatSlotPriority.of("pickLocation",20),
                ChatSlotPriority.of("pickContact",20),
                ChatSlotPriority.of("dropLocation",30),
                ChatSlotPriority.of("deliveryNote",40),
                ChatSlotPriority.of("weekDay",50)
        );
        requestDeliveryES.slotPriorities(slotPriorities);
        // Request Delivery Intent - Utterances
        ChatUtterance.of(requestDeliveryES,
                RequestDeliveryESUtterances.values().toArray(String[]::new));
    }


}
