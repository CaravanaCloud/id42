package id42.intent;

import id42.chat.*;
import id42.intent.es.RequestDeliveryESUtterances;
import id42.intent.es.SlotTypesES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ESChat {
    public static ChatLocale add(Chats chats) {
        var es = ChatLocale.of("es_ES",
                0.75,
                "Lucia");
        SlotTypesES.of(es);
        addEsEsIntents(chats, es);
        chats.locales().add(es);
        return es;
    }

    private static void addEsEsIntents(Chats chats, ChatLocale es) {
        // Slots
        var slotsMap = createSlotsMap(chats);

        addRequestDeliveryESIntent(es, slotsMap);
        // Check Delivery Intent
    }

    private static HashMap<String, ChatSlot> createSlotsMap(Chats chats) {
        var pickDateSlot = chats.requiredSlot("pickDate",
                "Fecha de recogida",
                "AMAZON.Date",
                "¿Que dia lo recogemos?");

        var pickTimeSlot = chats.requiredSlot("pickTime",
                "Hora de recogida",
                "AMAZON.Time",
                "¿A que horas recogemos?");

        var pickAddressSlot = chats.requiredSlot("pickAddress",
                "Local de recogida",
                "ID42_LOCATION",
                "¿En que direccion recogemos?");

        var pickAddressDetailSlot = chats.slot("pickAddressDetail",
                "Detalles (Escalera/Piso/Puerta) de recogida",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Hay Escalera/Piso/Puerta de recogida?",
                0);

        var pickSpotSlot = chats.slot("pickSpot",
                "Mas detalhes de recogida (neversa #3, ...)",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Algun local especifico a recoger?",
                0);

        var dropAddressSlot = chats.requiredSlot("dropAddress",
                "Direccion de entrega",
                "AMAZON.AlphaNumeric",
                "¿En que dirección entregamos?");

        var dropAddressDetailSlot = chats.slot("dropAddressDetail",
                "Detalles (Escalera/Piso/Puerta) de entrega",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Hay Escalera/Piso/Puerta de entrega?",
                0);

        var dropSpotSlot = chats.slot("dropSpot",
                "Mas detalhes de entrega (neversa #3, ...)",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Algun local especifico a entregar?",
                0);

        var pickContactSlot = chats.slot("pickContact",
                "Contacto para recoger el pedido",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Con quien recogemos?",
                0);

        var dropContactSlot = chats.slot("pickContact",
                "Contacto para dejar la entrega",
                "AMAZON.AlphaNumeric",
                "Optional",
                "¿Con quien lo dejamos?",
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

        var slotsMap = new HashMap<String, ChatSlot>() {{
            put("pickTime", pickTimeSlot);
            put("pickDate", pickDateSlot);
            put("pickAddress", pickAddressSlot);
            put("pickAddressDetail", pickAddressDetailSlot);
            put("pickSpot", pickSpotSlot);
            put("pickContact", pickContactSlot);
            put("dropAddress", dropAddressSlot);
            put("dropAddressDetail", dropAddressDetailSlot);
            put("dropSpot", dropSpotSlot);
            put("dropContact", dropContactSlot);
            put("weekDay", weekdaySlot);
            put("deliveryNote", deliveryNoteSlot);
        }};

        return slotsMap;
    }

    private static void addRequestDeliveryESIntent(ChatLocale es, Map<String, ChatSlot> slotsMap) {
        // Request Delivery Intent - Slots
        var requestDeliveryES = ChatIntent.of("RequestDeliveryES", es);

        requestDeliveryES.slots(slotsMap.values()
                .stream()
                .collect(Collectors.toList()));

        var slotPriorities = List.of(
                ChatSlotPriority.of("pickDate", 20),
                ChatSlotPriority.of("pickTime", 20),
                ChatSlotPriority.of("pickAddress", 20),
                ChatSlotPriority.of("pickAddressDetail", 20),
                ChatSlotPriority.of("pickSpot", 20),
                ChatSlotPriority.of("dropAddress", 20),
                ChatSlotPriority.of("dropAddressDetail", 20),
                ChatSlotPriority.of("dropSpot", 20),
                ChatSlotPriority.of("pickContact", 20),
                ChatSlotPriority.of("dropLocation", 30),
                ChatSlotPriority.of("deliveryNote", 40),
                ChatSlotPriority.of("weekDay", 50)
        );
        requestDeliveryES.slotPriorities(slotPriorities);
        // Request Delivery Intent - Utterances
        ChatUtterance.of(requestDeliveryES,
                RequestDeliveryESUtterances.values().toArray(String[]::new));
    }

}
