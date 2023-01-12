package id42.lex;

import id42.chat.ChatInteraction;
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

    public ChatInteraction ask(Input input) {
        try(var lex = lex()){
            var userInput = input.text();
            var sessionId = input.sessionId();
            var request = getRecognizeTextRequest(sessionId, userInput);
            var response = lex.recognizeText(request);
            var outcome = parseResponse(response);
            return outcome;
        }catch (Exception e){
            e.printStackTrace();
            return ChatInteraction.fail("Ops, something went wrong (%s)".formatted(e.getMessage()));
        }
    }

    private ChatInteraction parseResponse(RecognizeTextResponse resp) {
        var responseText = new StringBuilder();
        var logMap = new HashMap<>();
        var sessionState = resp.sessionState();
        var intent = sessionState.intent();
        var intentState = (IntentState) null;
        var slotsOut = new HashMap<String, String>();
        var intentName = (String) null;
        if (intent != null) {
            intentName = intent.name();
            intentState = intent.state();
            if (config.debugMode()){
                responseText.append("[");
                responseText.append(intentName);
                responseText.append(":");
                responseText.append(intentState);
                responseText.append("@");
                responseText.append(resp.sessionId());
                //responseText.append("_");
                //responseText.append(resp.sessionState());
                responseText.append("[");
            }
            logMap.put("intent.name", intentName);
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
                responseText.append("OK, request understood! Please wait a while for confirmation...\n");
            }
        }
        var messages = resp.messages();
        logMap.put("messagesCount", messages.size());
        messages.forEach(message -> {
            System.out.println("Bot : " + message.content());
            responseText.append(message.content() + "\n");
        });
        var responseStr = responseText.toString();
        if (responseStr.isBlank()){
            responseStr += "Sorry, not sure i got that. Please try again.";
        }
        log.info("lex resp: \n {}", logMap);
        if (intentState == null)
            return ChatInteraction.empty();
        var outcome = switch(intentState){
            case READY_FOR_FULFILLMENT -> ChatInteraction.ready(intentName, responseStr, slotsOut);
            case IN_PROGRESS -> ChatInteraction.partial(intentName, responseStr, slotsOut);
            case WAITING -> ChatInteraction.partial(intentName,"Waiting...", slotsOut);
            case FULFILLED -> ChatInteraction.ready(intentName,"Fulfilled...", slotsOut);
            case UNKNOWN_TO_SDK_VERSION -> ChatInteraction.fail("Unknown to sdk version");
            case FULFILLMENT_IN_PROGRESS -> ChatInteraction.partial(intentName,"Fulfillment in progress...", slotsOut);
            case FAILED -> ChatInteraction.fail(responseStr);
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
