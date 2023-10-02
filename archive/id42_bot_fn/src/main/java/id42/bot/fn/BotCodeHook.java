package id42.bot.fn;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

@Named("id42_bot_fn")
public class BotCodeHook implements RequestHandler<Map<String, Object>, Map<String, Object>> {


    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        return Map.of();
    }
}
