package id42.lex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id42.Identity;
import id42.chat.ChatRequest;
import id42.chat.SlotKey;
import id42.service.ChatInteractionService;
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
import java.util.HashMap;
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
    LEX lex;

    @Inject
    ObjectMapper mapper;

    @Inject
    SlotOverrides slots;

    @Inject
    ChatInteractionService chatService;

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
        try {
            var json = toJson(update);
            log.info("JSON update");
            log.info(json);
            telegramService.ingest(json);
            log.info("Message ingested by service.");
            var msg = message(update);
            if (msg != null) handleRequest(msg);
            log.info("Update completed.");
        } catch (Exception e) {
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

    public void sendText(Long who, String what) {
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

    public Message message(Update update) {
        var msg = update.getMessage();
        if (msg != null) return msg;
        msg = update.getEditedMessage();
        return msg;
    }

    private void handleRequest(Message msg) {
        log.info("Ingesting message {} by bot: {}", msg.getMessageId(), msg);
        var sessionId = "" + msg.getChatId();
        var inText = msg.getText();
        var identity = TelegramIdentity.of(msg.getFrom());
        var response = handleRequest(identity, sessionId, inText);
        var text = response.text();
        replyChat(msg, text);
        log.debug("Listener.handeRequest({}) completed.", msg.getMessageId());
    }

    private void replySlots(Message msg, ChatRequest intent) {
        var buf = new StringBuilder();
        buf.append("--- slots ---\n");
        for (var out : intent.slots().entrySet()) {
            var key = out.getKey();
            var val = out.getValue();
            buf.append(key)
                    .append(": ")
                    .append(val)
                    .append("\n");
        }
        var outText = buf.toString();
        replyChat(msg, outText);
    }

    private void triggerChatInteraction(ChatRequest interaction) {
        bm.fireEvent(interaction);
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

    private Function<Input, ChatRequest> of(Input input) {
        log.info("Input: {}", input);
        return switch (input.command()) {
            case "/salve" -> this::salve;
            default -> this::ask;
        };
    }

    public ChatRequest handleRequest(Input input) {
        var fn = of(input);
        if (fn == null) {
            log.warn("No function found for input: {}", input);
            return ChatRequest.empty();
        }
        var chat = fn.apply(input);
        if (chat != null) triggerChatInteraction(chat);
        return chat;
    }


    private ChatRequest sorry(Input input) {
        return ChatRequest.fail("Sorry, i didn't get that...");
    }

    private ChatRequest ask(Input input) {
        var text = input.prompt();
        var sessionId = input.sessionId();
        log.trace("Asking lex@{}: [{}]", sessionId, text);
        var response = lex.ask(input);
        return response;
    }


    private ChatRequest salve(Input input) {
        //TODO add identity to input
        // var msg = "Salve!".formatted(message.identity().name());
        var message = "Salve!";
        return ChatRequest.ready("salve", message, Map.of(), input.sessionId());
    }

    public void onChatInteraction(@Observes ChatRequest intent) {
        log.debug("ChatInteraction received: {}", intent);
    }

    public ChatRequest handleRequest(Identity identity,
                                     String sessionId,
                                     String text) {
        if (text == null || text.isBlank()) return null;
        var transform = slots.transform(text);
        var input = Input.of(identity,
                sessionId,
                transform.outputText());
        var chat = handleRequest(input);
        var lexSlots = chat.slots();
        var localSlots = transform.slots();
        var merged = mergeMaps(lexSlots, localSlots);
        chat = chat.withSlots(merged);
        chatService.accept(chat);
        return chat;
    }

    private HashMap<SlotKey, Object> mergeMaps(Map<SlotKey, Object> outSlots,
                                               Map<SlotKey, Object> txSlots) {
        var merged = new HashMap<SlotKey, Object>();
        merged.putAll(outSlots);
        merged.putAll(txSlots);
        return merged;
    }
}
