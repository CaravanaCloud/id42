package id42;

import org.telegram.telegrambots.meta.api.objects.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.function.Consumer;


@ApplicationScoped
public class Commands {
    @Inject
    Listener msgs;


}
