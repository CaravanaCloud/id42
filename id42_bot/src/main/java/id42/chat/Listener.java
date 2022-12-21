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

    @Override
    public String getBotUsername() {
        return config.username();
    }

    @Override
    public String getBotToken() {
        return config.token();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received.");
        var json = toJson(update);
        log.info("JSON update");
        log.info(json);
        telegramService.ingest(json);
        log.info("Message ingested by service.");
        var msg = message(update);
        if (msg != null) ingest(msg);
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

    @Inject
    HAL hal;

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
        log.info("text: {}", msg.getText());
        log.info("command: {}", input.command());
        log.info("prompt: {}", input.prompt());
        return switch (input.command()){
            case "/ask" -> this::ask;
            case "/salve" -> this::salve;
            default -> this::ok;
        };

    }

    private void ask(Message message) {
        var input = Input.of(message);
        var response = hal.ask(input);
        replyChat(message, response);
    }

    private void ok(Message message) {
        replyFrom(message, "OK...");
    }

    private void salve(Message message) {
        replyChat(message, "Salve, %s!".formatted(message.getFrom().getFirstName()));
    }
}