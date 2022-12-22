package id42.chat.intent;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named("request-delivery")
public class RequestDeliveryIntent extends SimpleIntent {

    @Override
    public Outcome apply(Slots slots) {
        return new Outcome("OK, adding that to your delivery request...");
    }
}
