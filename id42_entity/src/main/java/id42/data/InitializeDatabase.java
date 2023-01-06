package id42.data;

import id42.service.TelegramService;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class InitializeDatabase {
    @Inject
    TelegramService telegramService;

    public void onStart(@Observes StartupEvent event) {
        System.out.println("Initializing database - 20221912-204141");
        //TODO: fix invalid json var jsonUpdate="{'update_id': 123456789, 'message': {'message_id': 123, 'from': {'id': 123456789, 'is_bot': false, 'first_name': 'John', 'last_name': 'Doe', 'language_code': 'en'}, 'chat': {'id': 123456789, 'first_name': 'John', 'last_name': 'Doe', 'type': 'private'}, 'date': 1600000000, 'text': 'Hello, world!'}}";
        //telegramService.ingest(jsonUpdate);
    }
}
