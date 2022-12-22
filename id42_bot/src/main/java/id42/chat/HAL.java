package id42.chat;

import id42.chat.intent.Intent;
import id42.chat.intent.Intents;
import id42.chat.intent.Slots;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@ApplicationScoped
public class HAL {
    @Inject
    Logger log;
    @Inject
    Intents intents;

    @Inject
    Slots slots;

    @Inject
    BeanManager bm;

    public String ask(String prompt) {
        var input = Input.of(prompt);
        var intentConfig = intents.of(input);
        if (intentConfig.isEmpty())
            return "I'm sorry, i did not understand your intent.";
        var intentName = intentConfig.get().name();
        var bean = bm.getBeans(intentName)
                .stream()
                .findAny();
        if (bean.isEmpty())
            return "I'm sorry, i failed to process your intent.";
        var intent = fomBean(bean.get());
        var params = slots.parse(prompt);
        var outcome = intent.apply(slots);
        return outcome.message();
    }

    private Intent fomBean(Bean<?> bean) {
        var ctx = bm.createCreationalContext(bean);
        try {
            var ref = bm.getReference(bean, bean.getBeanClass(), ctx);
            if (ref instanceof Intent intent) {
                    return intent;
                } else {
                    log.error("Bean {} is not an Intent", bean);
                    throw new IllegalArgumentException("Bean is not a task");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Failed to create task from bean {}", bean, ex);
                throw ex;
            }
    }

    public String ask(Input input) {
        return "OK, thanks for asking...";
    }
}
