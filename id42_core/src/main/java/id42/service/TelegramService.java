package id42.service;

import id42.entity.TelegramMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class TelegramService {
    @Transactional
    public void ingest(String jsonMessage) {
        System.out.println("Ingesting message: " + jsonMessage);
        var msg = new TelegramMessage(jsonMessage);
        msg.persist();
        System.out.println("Message ingested: " + jsonMessage);

    }
}
