package id42.chat.intent;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class Slots {
    public Map<Slot, String> parse(String prompt){
        return Map.of();
    }
}
