package id42.intent;

import id42.chat.SlotKey;

public enum DeliverySlot implements SlotKey {
    // delivery slots
    locator,
    pickDate,
    pickTime,
    pickAddress,
    pickAddressDetail,
    pickSpot,
    pickContact,
    dropAddress,
    dropAddressDetail,
    dropSpot,
    dropContact,
    weekDay,
    deliveryNote,
    dropLocation

}
