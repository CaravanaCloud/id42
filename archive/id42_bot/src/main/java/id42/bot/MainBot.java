package id42.bot;

import id42.bot.lex.QuarkusBot;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MainBot {
    public static void main(String[] args) {
        Quarkus.run(QuarkusBot.class, args);
    }
}
