package id42.chat;

import java.util.List;
import java.util.stream.Collectors;

public class ChatSlotType {
    String name;
    String description;
    ChatValueSelectionSetting valueSelectionSetting;
    List<ChatSlotTypeValue> slotTypeValues;

    public ChatSlotType(String name, String description, ChatValueSelectionSetting valueSelectionSetting, List<ChatSlotTypeValue> toList) {
        this.name = name;
        this.description = description;
        this.valueSelectionSetting = valueSelectionSetting;
        this.slotTypeValues = toList;
    }

    public static ChatSlotType of(String name,
                                  String description,
                                  ChatValueSelectionSetting valueSelectionSetting,
                                  List<String> slotTypeValues) {
        var chatSlotType = new ChatSlotType(name,
                description,
                valueSelectionSetting,
                slotTypeValues
                        .stream()
                        .map( x -> ChatSlotTypeValue.of(x))
                        .collect(Collectors.toList()));
        return chatSlotType;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public ChatValueSelectionSetting valueSelectionSetting() {
        return valueSelectionSetting;
    }

    public List<ChatSlotTypeValue> slotTypeValues() {
        return slotTypeValues;
    }
}
