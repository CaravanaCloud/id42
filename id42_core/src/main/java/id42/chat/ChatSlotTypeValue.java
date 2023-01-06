package id42.chat;

import java.util.List;

public class ChatSlotTypeValue {

     private final String value;
     private final List<String> synonyms;

     public ChatSlotTypeValue(String value, List<String> synonyms) {
          this.value = value;
          this.synonyms = synonyms;
     }

     public static ChatSlotTypeValue of(String value) {
        var chatSlotTypeValue = new ChatSlotTypeValue(value, null);
        return chatSlotTypeValue;
    }

    public String sampleValue() {
         return value;
    }

    public List<String> synonyms() {
         return synonyms;
    }
}
