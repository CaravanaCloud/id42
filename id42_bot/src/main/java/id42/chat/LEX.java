package id42.chat;

import org.slf4j.Logger;
import software.amazon.awssdk.services.lexruntimev2.LexRuntimeV2Client;
import software.amazon.awssdk.services.lexruntimev2.model.IntentState;
import software.amazon.awssdk.services.lexruntimev2.model.RecognizeTextRequest;

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
            var recognizeTextRequest = getRecognizeTextRequest(sessionId, userInput);
            var response = lex.recognizeText(recognizeTextRequest);
            var responseText = new StringBuilder();
            var logMap = new HashMap<>();
            var sessionState = response.sessionState();
            var intent = sessionState.intent();
            if (intent != null) {
                logMap.put("intentName", intent.name());
                var intentState = intent.state();
                responseText.append("["+intentState+"] ");
                logMap.put("intent.name", intent.name());
                logMap.put("intent.state", intentState.toString());
                if (intent.hasSlots()) {
                    var slots = intent.slots();
                    slots.forEach((name, slot) -> {
                        var valueStr = "";
                        if (slot != null){
                            var value = slot.value();
                            valueStr = value.interpretedValue().toString();
                        }
                        logMap.put("slot."+name, valueStr);
                    });
                }
                if (IntentState.READY_FOR_FULFILLMENT.equals(intentState)){
                    responseText.append("OK, delivery request understood! Please wait a while for confirmation...\n");
                    responseText.append(" here is the data i received: \n");
                    responseText.append(logMap);
                }
            }
            System.out.println("User : " + userInput);
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
            return Outcome.ok(responseStr);
        }catch (Exception e){
            e.printStackTrace();
            return Outcome.fail("Ops, something went wrong (%s)".formatted(e.getMessage()));
        }
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
