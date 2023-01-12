package id42.service;

import id42.chat.ChatInteraction;
import id42.chat.IntentKey;
import id42.entity.Delivery;
import id42.intent.ID42Intents;
import id42.intent.ID42Slots;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ApplicationScoped
public class ChatInteractionService extends Service {
    @Inject
    EntityManager em;

    Map<IntentKey, Consumer<ChatInteraction>> intentConsumers = new HashMap<>();

    public void init(@Observes StartupEvent evt){
        intentConsumers.put(ID42Intents.RequestDeliveries, this::requestDeliveries);
    }

    public void consume(ChatInteraction chat){
        var intent = chat.intent();
        var consumer = intentConsumers.get(intent);
        if(consumer != null){
            consumer.accept(chat);
            log.info("Resolved intent: {}", intent);
        }else {
            log.warn("No consumer for intent: {} ", intent);
        }
    }

    private void requestDeliveries(ChatInteraction chat) {
        log.info("Requesting deliveries");
        var deliveries = chat.slotList(Delivery.class, ID42Slots.deliveries);
        log.info("Found " + deliveries.size() + " deliveries");
    }

}
