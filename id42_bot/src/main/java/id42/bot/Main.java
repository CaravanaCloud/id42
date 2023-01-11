package id42.chat.bot;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String[] args) {
        Quarkus.run(QuarkusBot.class, args);
    }
}
