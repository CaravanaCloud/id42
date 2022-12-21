package id42.chat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class Commands {
    @Inject
    Listener msgs;


}
