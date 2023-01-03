package id42.cdk;

import id42.cdk.chat.*;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.lex.CfnBot;
import software.amazon.awscdk.services.lex.CfnBotAlias;
import software.amazon.awscdk.services.lex.CfnBotVersion;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BotLexStack extends Stack {
    private String botId;
    private String botAlias;

    public BotLexStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public BotLexStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var chats = ID42Chat.of();
        var locales = locales(chats);

        var managedPolicyArn = "arn:aws:iam::aws:policy/AdministratorAccess";
        var adminPolicy = ManagedPolicy.fromManagedPolicyArn(this,
                "ChatBotPolicy", managedPolicyArn);

        var role = Role.Builder.create(this, "id42-bot-lex-role")
                    .assumedBy(ServicePrincipal.Builder.create("lexv2.amazonaws.com").build())
                .managedPolicies(List.of(adminPolicy))
                .build();

        var privacy = CfnBot.DataPrivacyProperty.builder()
                .childDirected(false)
                .build();

        /*
        var textLogSettings = CfnBot.TextLogSettingProperty.builder()
                .enabled(true)
                .build();

        var conversationLogSettings = CfnBot.ConversationLogSettingsProperty
                .builder()
                .textLogSettings(List.of(textLogSettings))
                .build();

        var testBotAliasSetting = CfnBot.TestBotAliasSettingsProperty.builder()
                .conversationLogSettings(conversationLogSettings)
                .build();
        */
        var bot = CfnBot.Builder.create(this, "id42-lex-bot")
                .name("id42-bot")
                .botLocales(locales)
                .autoBuildBotLocales(true)
                .roleArn(role.getRoleArn())
                .dataPrivacy(privacy)
                .idleSessionTtlInSeconds(86400)
        //        .testBotAliasSettings(testBotAliasSetting)
                .build();
        this.botId = bot.getRef();
        // Version
        /*
        var versionLocaleDetailsES = CfnBotVersion.BotVersionLocaleDetailsProperty
                .builder()
                .sourceBotVersion("$LATEST")
                .build();

        var versionLocaleSpecES = CfnBotVersion.BotVersionLocaleSpecificationProperty
                .builder()
                .localeId("es-ES")
                .botVersionLocaleDetails(versionLocaleDetailsES)
                .build();

        var version = CfnBotVersion.Builder.create(this, "id42-lex-bot-version-latest")
                .botId(this.botId)
                .description("Latest version")
                .botVersionLocaleSpecification(List.of(versionLocaleSpecES))
                .build();
        */
        // Alias
        var aliasLocaleES = CfnBotAlias.BotAliasLocaleSettingsItemProperty.builder()
                .localeId("es_ES")
                .botAliasLocaleSetting(CfnBotAlias.BotAliasLocaleSettingsProperty.builder()
                        .enabled(true)
                        .build())
                .build();

        var alias = CfnBotAlias.Builder.create(this, "id42-lex-bot-alias-latest")
                .botAliasName("id42-bot-alias-latest")
                .botAliasLocaleSettings(List.of(aliasLocaleES))
                .botId(bot.getAtt("Id").toString())
                .build();

        alias.addDependency(bot);
        this.botAlias = alias.getAttrBotAliasId();

        var aliasIdOut = CfnOutput.Builder.create(this, "id42-lex-bot-alias-id")
                .value(this.botAlias)
                .build();


        var botIdOut = CfnOutput.Builder.create(this, "id42-lex-bot-id")
                .value(this.botId)
                .build();

    }

    public List<CfnBot.BotLocaleProperty> locales(Chats chats){
        return chats.locales()
                .stream()
                .map(this::toBotLocaleProperty)
                .collect(Collectors.toList());
    }

    private CfnBot.BotLocaleProperty toBotLocaleProperty(ChatLocale chatLocale) {
        var intents = chatLocale.intents()
                .stream()
                .map(this::toIntent)
                .collect(Collectors.toList());
        var lexIntents = new ArrayList<>(intents);
        var fallbackIntent = CfnBot.IntentProperty.builder()
                .name("FallbackIntent")
                .description("Default intent when no other intent matches")
                .parentIntentSignature("AMAZON.FallbackIntent")
                .build();
        lexIntents.add(fallbackIntent);
        var voice = toVoice(chatLocale.voice());
        return CfnBot.BotLocaleProperty.builder()
                .localeId(chatLocale.localeId())
                .nluConfidenceThreshold(chatLocale.nluConfidenceThreshold())
                .voiceSettings(voice)
                .intents(lexIntents)
                .slotTypes(toSlotTypes(chatLocale.slotTypes()))
                .build();
    }

    private List<? extends Object> toSlotTypes(List<ChatSlotType> slotTypes) {
        return slotTypes.stream()
                .map(this::toSlotType)
                .collect(Collectors.toList());
    }

    private CfnBot.SlotTypeProperty toSlotType(ChatSlotType chatSlotType) {
        return CfnBot.SlotTypeProperty.builder()
                .name(chatSlotType.name())
                .description(chatSlotType.description())
                .valueSelectionSetting(toValueSelectionSetting(chatSlotType.valueSelectionSetting()))
                .slotTypeValues(toSlotTypeValues(chatSlotType.slotTypeValues()))
                .build();
    }

    private List<? extends Object> toSlotTypeValues(List<ChatSlotTypeValue> slotTypeValues) {
        return  slotTypeValues.stream()
                .map(this::toSlotTypeValue)
                .collect(Collectors.toList());
    }

    private CfnBot.SlotTypeValueProperty toSlotTypeValue(ChatSlotTypeValue chatSlotTypeValue) {
        return CfnBot.SlotTypeValueProperty.builder()
                .sampleValue(toSampleValue(chatSlotTypeValue.sampleValue()))
                .synonyms(chatSlotTypeValue.synonyms())
                .build();
    }

    private CfnBot.SampleValueProperty toSampleValue(String sampleValue) {
        return CfnBot.SampleValueProperty.builder()
                .value(sampleValue)
                .build();
    }

    private CfnBot.SlotValueSelectionSettingProperty toValueSelectionSetting(ChatValueSelectionSetting valueSelectionSetting) {
        return CfnBot.SlotValueSelectionSettingProperty.builder()
                .resolutionStrategy(valueSelectionSetting.resolutionStrategy())
                .build();
    }

    private CfnBot.VoiceSettingsProperty toVoice(String voiceName) {
        var voice = CfnBot.VoiceSettingsProperty.builder()
                .voiceId(voiceName)
                .build();
        return voice;
    }

    private CfnBot.IntentProperty toIntent(ChatIntent chatIntent) {
        return CfnBot.IntentProperty.builder()
                .name(chatIntent.name())
                .description(chatIntent.description())
                .sampleUtterances(toUtterances(chatIntent.utterances()))
                .slots(toSlots(chatIntent.slots()))
                .slotPriorities(toSlotPriorities(chatIntent.slotPriorities()))
                .build();
    }

    private List<CfnBot.SlotPriorityProperty> toSlotPriorities(List<ChatSlotPriority> slotPriorities) {
        return slotPriorities.stream()
                .map(this::toSlotPriority)
                .collect(Collectors.toList());
    }

    private CfnBot.SlotPriorityProperty toSlotPriority(ChatSlotPriority chatSlotPriority) {
        return CfnBot.SlotPriorityProperty.builder()
                .slotName(chatSlotPriority.slotId())
                .priority(chatSlotPriority.priority())
                .build();
    }

    private List<CfnBot.SampleUtteranceProperty> toUtterances(List<ChatUtterance> utterances) {
        return utterances.stream().map(this::toUtterance).collect(Collectors.toList());
    }

    private CfnBot.SampleUtteranceProperty toUtterance(ChatUtterance utterance) {
        return CfnBot.SampleUtteranceProperty.builder()
                .utterance(utterance.value())
                .build();
    }

    private List<? extends Object> toSlots(List<ChatSlot> slots) {
        return slots.stream()
                .map(this::toSlot)
                .collect(Collectors.toList());
    }

    private CfnBot.SlotProperty toSlot(ChatSlot chatSlot) {
        var slot = CfnBot.SlotProperty.builder()
                .name(chatSlot.name())
                .description(chatSlot.description())
                .slotTypeName(chatSlot.type())
                .valueElicitationSetting(toElicitation(chatSlot.elicitation()))
                .build();
        return slot;
    }

    private CfnBot.PromptSpecificationProperty toPromptSpec(ChatPrompt prompt){
        var promptSpec = CfnBot.PromptSpecificationProperty.builder()
                .messageGroupsList(toMessageGroups(prompt.messageGroups()))
                .maxRetries(prompt.maxRetries())
                .build();
        return promptSpec;
    }

    private List<CfnBot.MessageGroupProperty> toMessageGroups(List<ChatPromptMessageGroup> messageGroups) {
        return messageGroups.stream()
                .map(this::toMessageGroup)
                .collect(Collectors.toList());
    }

    private CfnBot.MessageGroupProperty toMessageGroup(ChatPromptMessageGroup chatPromptMessageGroup) {
        var msgGroup = CfnBot.MessageGroupProperty.builder()
                .message(toMessage(chatPromptMessageGroup.message()))
                .build();
        return msgGroup;
    }

    private List<CfnBot.MessageProperty> toMessages(List<ChatPromptMessage> messages) {
        return messages.stream()
                .map(this::toMessage)
                .collect(Collectors.toList());
    }

    private CfnBot.MessageProperty toMessage(ChatPromptMessage chatPromptMessage) {
        return CfnBot.MessageProperty.builder()
                .plainTextMessage(toPlainTextMessage(chatPromptMessage))
                .build();
    }

    private CfnBot.PlainTextMessageProperty toPlainTextMessage(ChatPromptMessage chatPromptMessage) {
        return CfnBot.PlainTextMessageProperty.builder()
                .value(chatPromptMessage.message())
                .build();
    }

    private CfnBot.SlotValueElicitationSettingProperty toElicitation(ChatSlotElicitation elicitation) {
        var promptSpec = toPromptSpec(elicitation.prompt());
        return CfnBot.SlotValueElicitationSettingProperty.builder()
                .slotConstraint(elicitation.slotConstraint())
                .promptSpecification(promptSpec)
                .build();
    }

    public String botId() {
        return this.botId;
    }

    public String botAlias() {
        return this.botAlias;
    }
}
