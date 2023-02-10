package id42.to;

import id42.chat.SlotKey;
import id42.chat.Slots;
import id42.entity.Delivery;

public class ChatResponse {
    public static ChatResponse empty() {
        return new ChatResponse();
    }

    public <T> T get(SlotKey key, Class<T> type) {
        return null; //TODO (Julio): in accept(), return the produced data in the response.
    }
}
