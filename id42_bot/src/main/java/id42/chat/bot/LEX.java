package id42.chat.bot;

import org.slf4j.Logger;
import software.amazon.awssdk.services.lexruntimev2.LexRuntimeV2Client;
import software.amazon.awssdk.services.lexruntimev2.model.IntentState;
import software.amazon.awssdk.services.lexruntimev2.model.RecognizeTextRequest;
import software.amazon.awssdk.services.lexruntimev2.model.RecognizeTextResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;

@ApplicationScoped
public class LEX {
    @Inject
    BotConfig config;

    @Inject
    Logger log;

    public LexRuntimeV2Client lex(){
        var region = config.region();
        var lex = LexRuntimeV2Client
                .builder()
                .region(region)
                .build();
        return lex;
    }

    public Outcome ask(Input input) {
        try(var lex = lex()){
            var userInput = input.prompt().text();
            var sessionId = input.sessionId();
            var request = getRecognizeTextRequest(sessionId, userInput);
            var response = lex.recognizeText(request);
            var outcome = parseResponse(response);
            return outcome;
        }catch (Exception e){
            e.printStackTrace();
            return Outcome.fail("Ops, something went wrong (%s)".formatted(e.getMessage()));
        }
    }

    private Outcome parseResponse(RecognizeTextResponse response) {
        var responseText = new StringBuilder();
        var logMap = new HashMap<>();
        var sessionState = response.sessionState();
        var intent = sessionState.intent();
        var intentState = (IntentState) null;
        var slotsOut = new HashMap<String, String>();
        if (intent != null) {
            logMap.put("intentName", intent.name());
            intentState = intent.state();
            responseText.append("["+intentState+"] ");
            logMap.put("intent.name", intent.name());
            logMap.put("intent.state", intentState.toString());
            if (intent.hasSlots()) {
                var slots = intent.slots();
                slots.forEach((name, slot) -> {
                    var valueStr = "";
                    if (slot != null){
                        var value = slot.value();
                        var interpreted = value.interpretedValue();
                        if (interpreted != null)
                            valueStr = interpreted;
                        else
                            valueStr = value.originalValue();
                    }
                    logMap.put("slot."+name, valueStr);
                    slotsOut.put(name, valueStr);
                });
            }
            if (IntentState.READY_FOR_FULFILLMENT.equals(intentState)){
                responseText.append("OK, delivery request understood! Please wait a while for confirmation...\n");
                responseText.append(" here is the data i received: \n");
                responseText.append(logMap);
            }
        }
        var messages = response.messages();
        logMap.put("messagesCount", messages.size());
        messages.forEach(message -> {
            System.out.println("Bot : " + message.content());
            responseText.append(message.content() + "\n");
        });
        var responseStr = responseText.toString();
        if (responseStr.isBlank()){
            responseStr += "Sorry, not sure i got that. Please try again.";
        }
        log.info("lex response", logMap);
        if (intentState == null)
            return Outcome.empty();
        var outcome = switch(intentState){
            case READY_FOR_FULFILLMENT -> Outcome.ready(responseStr, slotsOut);
            case IN_PROGRESS -> Outcome.partial(responseStr, slotsOut);
            case WAITING -> Outcome.partial("Waiting...", slotsOut);
            case FULFILLED -> Outcome.ready("Fulfilled...", slotsOut);
            case UNKNOWN_TO_SDK_VERSION -> Outcome.fail("Unknown to sdk version");
            case FULFILLMENT_IN_PROGRESS -> Outcome.partial("Fulfillment in progress...", slotsOut);
            case FAILED -> Outcome.fail(responseStr);
        };
        return outcome;
    }

    private RecognizeTextRequest getRecognizeTextRequest(String sessionId,
                                                         String userInput) {
        var botAliasId = config.lexBotAliasId();
        var botId  = config.lexBotId();
        var recognizeTextRequest = RecognizeTextRequest.builder()
                    .botAliasId(botAliasId)
                    .botId(botId)
                    .localeId(localeOf(sessionId))
                    .sessionId(sessionId)
                    .text(userInput)
                    .build();
        return recognizeTextRequest;
    }

    private String localeOf(String sessionId) {
        return "es_ES";
    }
}
