package id42.intent;

import id42.chat.SlotKey;

import java.util.List;

public enum ID42Slots implements SlotKey {
    // delivery request slots
    deliveries,
    // delivery slots
    pickupDate,
    pickupTime,
    pickupLocation,
    pickupContact,
    weekDay,
    deliveryNote,
    dropLocation;
}
