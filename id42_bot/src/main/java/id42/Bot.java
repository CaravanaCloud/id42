package id42;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class Bot extends TelegramLongPollingBot {
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
            var text = msg.getText();
            var command = text.split("@")[0];
            System.out.println("command = " + command);
            var response = switch (command){
                case "/salve" -> salve(user);
                default -> "OK...";
            };
            var groupMessage = msg.isGroupMessage();
            var groupChat = msg.getChat().isGroupChat();
            var chatId = msg.getChatId();
            var userId = user.getId();

            System.out.println("groupMessage = " + groupMessage);
            System.out.println("groupChat = " + groupChat);
            System.out.println("chatId = " + chatId);
            System.out.println("userId = " + userId);

            sendText(chatId, response);
            sendText(user, response);
        }
    }

    private String salve(User user) {
        return  "Salve, " + user.getFirstName() + "!";
    }

    public void sendText(User user, String what){
        sendText(user.getId(), what);
    }

    public void sendText(Long who, String what){
        var sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}