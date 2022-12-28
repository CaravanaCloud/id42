package id42.chat;

import software.amazon.awssdk.services.lexruntimev2.LexRuntimeV2Client;
import software.amazon.awssdk.services.lexruntimev2.model.RecognizeTextRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LEX {
    @Inject
    BotConfig config;

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
            var recognizeTextResponse = lex.recognizeText(recognizeTextRequest);
            var responseText = new StringBuilder();
            var sessionState = recognizeTextResponse.sessionState();
            var intent = sessionState.intent();
            var intentState = intent.state();

            responseText.append("["+intentState+"] ");
            System.out.println("User : " + userInput);
            recognizeTextResponse.messages().forEach(message -> {
                System.out.println("Bot : " + message.content());
                responseText.append(message.content() + "\n");
            });
            var responseStr = responseText.toString();
            if (responseStr.isBlank()){
                responseStr += "Ok.";
            }
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
