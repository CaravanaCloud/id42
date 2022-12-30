package id42.chat;

import id42.Identity;
import id42.chat.bot.Input;

public abstract class ChatTest {

    protected Identity identity() {
        return new Identity() {
        };
    }

    protected Input input(String message) {
        return Input.of(identity(), message);
    }

}
