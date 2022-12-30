package id42.cdk.chat;

import java.util.List;

public record ChatSlotTypeValue(String sampleValue,
                                List<String> synonyms) {

     public static ChatSlotTypeValue of(String value) {
        var chatSlotTypeValue = new ChatSlotTypeValue(value, null);
        return chatSlotTypeValue;
    }

}
