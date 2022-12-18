package id42;

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
        System.out.println(update);
        var msg = message(update);
        if (msg != null) ingest(msg);
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
        var user = msg.getFrom();
        System.out.println(user.getFirstName() + " wrote " + msg.getText());
        var isCmd = msg.isCommand();
        System.out.println("isCmd = " + isCmd);
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
        var text = msg.getText();
        var command = text.split("@")[0];
        return switch (command){
            case "/salve" -> this::salve;
            default -> this::ok;
        };

    }

    private void ok(Message message) {
        replyFrom(message, "OK...");
    }

    private void salve(Message message) {
        replyChat(message, "Salve, %s!".formatted(message.getFrom().getFirstName()));
    }
}
