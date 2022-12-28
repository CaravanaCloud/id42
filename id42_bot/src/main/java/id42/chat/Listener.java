package id42.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id42.service.TelegramService;
import org.slf4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.function.Consumer;

@ApplicationScoped
public class Listener extends TelegramLongPollingBot {
    @Inject
    BotConfig config;

    @Inject
    Logger log;

    @Inject
    TelegramService telegramService;

    @Inject
    HAL hal;

    @Inject
    LEX lex;


    @Override
    public String getBotUsername() {
        return config.username().orElse(null);
    }

    @Override
    public String getBotToken() {
        return config.token().orElse(null);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received.");
        try{
            var json = toJson(update);
            log.info("JSON update");
            log.info(json);
            telegramService.ingest(json);
            log.info("Message ingested by service.");
            var msg = message(update);
            if (msg != null) ingest(msg);
            log.info("Update completed.");
        }catch (Exception e){
            log.error("Error processing update.", e);
        }
    }

    @Inject
    ObjectMapper mapper;
    private String toJson(Update update) {
        String json = null;
        try {
            json = mapper.writeValueAsString(update);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return json;
    }

    public void sendText(Long who, String what){
        if (who == null) {
            log.warn("Destination not set for message");
            return;
        }
        if (what == null || what.isBlank()) {
            log.warn("Attempting to send empty message.");
            return;
        }
        var sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Message message(Update update){
        var msg = update.getMessage();
        if (msg != null) return msg;
        msg = update.getEditedMessage();
        return msg;
    }

    private void ingest(Message msg) {
        log.info("Ingesting message by bot.");
        var isCmd = msg.isCommand();
        log.info("isCmd = {}", isCmd);
        if(isCmd){
            var cmd = of(msg);
            cmd.accept(msg);
        }
    }



    public void replyFrom(Message msg, String s) {
        var user = msg.getFrom();
        var userId = user.getId();
        System.out.println("userId = " + userId);
        sendText(userId, s);
    }

    public void replyChat(Message msg, String s) {
        var groupMessage = msg.isGroupMessage();
        var groupChat = msg.getChat().isGroupChat();
        var chatId = msg.getChatId();
        System.out.println("groupMessage = " + groupMessage);
        System.out.println("groupChat = " + groupChat);
        System.out.println("chatId = " + chatId);
        sendText(chatId, s);
    }

    public Consumer<Message> of(Message msg){
        var input = Input.of(msg);
        var command = input.command().trim();
        var prompt = input.prompt();
        log.info("text: [{}]", msg.getText());
        log.info("command: [{}]", command);
        log.info("prompt: [{}][{}]", prompt, prompt );
        return switch (input.command()){
            case "/ask" -> this::ask;
            case "/salve" -> this::salve;
            default -> this::ok;
        };
    }

    private void ask(Message message) {
        var input = Input.of(message);
        var text = input.prompt().text();
        var response = (Outcome) null;
        if (text.startsWith(HAL.WAKEWORD)){
            response = hal.ask(input);
        } else {
            response = lex.ask(input);
        }
        replyChat(message, response.message());
    }

    private void misunderstood(Message message) {
        replyChat(message, "Sorry, i didn't understand that.");
    }

    private void ok(Message message) {
        replyFrom(message, "OK...");
    }

    private void salve(Message message) {
        replyChat(message, "Salve, %s!".formatted(message.getFrom().getFirstName()));
    }
}
