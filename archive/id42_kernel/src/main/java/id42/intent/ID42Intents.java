package id42.intent;

import id42.chat.IntentKey;

public enum ID42Intents implements IntentKey {
    RequestDeliveries,
    QueryDeliveries;

    public static IntentKey byName(String name) {
        //TODO: Parse intent name
        return RequestDeliveries;
    }
}
