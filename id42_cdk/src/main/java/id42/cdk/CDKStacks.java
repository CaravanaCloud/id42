package id42.cdk;

import id42.cdk.config.ChatConfig;
import id42.cdk.config.StaticConfig;
import io.smallrye.config.SmallRyeConfig;
import org.eclipse.microprofile.config.ConfigProvider;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class CDKStacks {

    public int run(String... args) throws Exception {
        System.out.println("Starting CDK Synth...");
        System.out.println("Config version: %s".formatted(StaticConfig.version.getString()));
        App app = new App();
        var props = StackProps.builder().build();
        var network = new NetworkStack(app, "Id42NetworkStack", props);
        var database = new DatabaseStack(app, "Id42DatabaseStack", props, network);
        var api = new APIStack(app, "Id42APIStack", props, network, database);
        var botApp = new BotApplicationStack(app, "Id42BotAppStack", props, database);
        var bastion = new BastionStack(app, "Id42BastionStack", props, network);
        var lex = new BotLexStack(app, "Id42LexStack", props);
        var botEnv = new BotEnvironmentStack(app, "Id42BotEnvironmentStack", props,  network, database, botApp, lex);
        app.synth();
        return 0;
    }
}
