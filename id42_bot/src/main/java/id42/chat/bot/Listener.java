package id42.chat.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id42.Identity;
import id42.service.TelegramService;
import org.slf4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class Listener extends TelegramLongPollingBot {
    @Inject
    BotConfig config;

    @Inject
    BeanManager bm;

    @Inject
    Logger log;

    @Inject
    TelegramService telegramService;

    @Inject
    HAL hal;

    @Inject
    LEX lex;

    @Inject
    ObjectMapper mapper;

    @Inject
    SlotOverrides slots;

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

    private Outcome ingest(Message msg) {
        log.info("Ingesting message by bot: {}", msg);
        var isCmd = msg.isCommand();
        if(isCmd){
            log.debug("Processing command message");
            var inText = msg.getText();
            var outcome = ingest(null, inText);
            var outText = outcome.message();
            replyChat(msg, outText);
            return outcome;
        }else {
            log.debug("Message is not a command, skipping");
        }
        return null;
    }

    private void triggerOutcome(Outcome outcome) {
        bm.fireEvent(outcome);
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

    private Function<Input, Outcome> of(Input input) {
        var command = input.command();
        var prompt = input.prompt();
        log.info("command: [{}]", command);
        log.info("prompt: [{}][{}]", prompt, prompt );
        return switch (input.command()) {
            case "/ask" -> this::ask;
            case "/salve" -> this::salve;
            default -> this::sorry;
        };
    }

    public Outcome ingest(Input input) {
        var fn = of(input);
        if (fn == null) return Outcome.empty();
        var outcome = fn.apply(input);
        if (outcome != null) triggerOutcome(outcome);
        return outcome;
    }

    private Outcome sorry(Input input) {
        return Outcome.fail("Sorry, i didn't get that...");
    }

    private Outcome ask(Message message) {
        var input = Input.of(message);
        var output = ask(input);
        replyChat(message, output.message());
        return output;
    }

    private Outcome ask(Input input) {
        var text = input.prompt().text();
        var response = (Outcome) null;
        var toHal = text.isBlank() ||
                text.startsWith(HAL.WAKEWORD);
        if (toHal){
            log.trace("Asking HAL");
            response = hal.ask(input);
        } else {
            log.trace("Asking LEX");
            response = lex.ask(input);
        }

        return response;
    }

    private void misunderstood(Message message) {
        replyChat(message, "Sorry, i didn't understand that.");
    }

    private void ok(Message message) {
        replyFrom(message, "OK...");
    }

    private Outcome salve(Input input) {
        //TODO add identity to input
        // var msg = "Salve!".formatted(message.identity().name());
        var message = "Salve!";
        return Outcome.ready(message, Map.of());
    }

    public void onOutcome(@Observes Outcome outcome){
        log.debug("Outcome received: {}", outcome);
    }

    public Outcome ingest(Identity identity, String text) {
        if (text == null || text.isBlank()) return Outcome.empty();
        var inputText = slots.transform(text);
        var input = Input.of(identity, inputText);
        return ingest(input);
    }
}
