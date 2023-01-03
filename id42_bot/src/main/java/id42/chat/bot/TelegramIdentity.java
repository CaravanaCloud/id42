package id42.chat.bot;

import id42.Identity;
import org.telegram.telegrambots.meta.api.objects.User;

public class TelegramIdentity implements Identity {

    public static Identity of(User from) {
        return new TelegramIdentity();
    }
}
