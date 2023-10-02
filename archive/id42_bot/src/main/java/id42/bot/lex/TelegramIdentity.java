package id42.bot.lex;

import id42.Identity;
import org.telegram.telegrambots.meta.api.objects.User;

public class TelegramIdentity implements Identity {

    public static Identity of(User from) {
        return new TelegramIdentity();
    }
}
