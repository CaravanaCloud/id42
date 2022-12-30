package id42.cdk.chat;

import java.util.List;

public record ChatSlotType(String name,
                           String description,
                           ChatValueSelectionSetting valueSelectionSetting,
                           List<ChatSlotTypeValue> slotTypeValues) {
    public static ChatSlotType of(String name,
                                  String description,
                                  ChatValueSelectionSetting valueSelectionSetting,
                                  List<String> slotTypeValues) {
        var chatSlotType = new ChatSlotType(name,
                description,
                valueSelectionSetting,
                slotTypeValues.stream().map( x -> ChatSlotTypeValue.of(x)).toList());
        return chatSlotType;
    }
}
