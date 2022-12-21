package id42;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class Id42CdkApp {
    public static void main(final String[] args) {
        App app = new App();
        var props = StackProps.builder().build();
        var network = new NetworkStack(app, "Id42NetworkStack", props);
        var database = new DatabaseStack(app, "Id42DatabaseStack", props, network);
        var api = new APIStack(app, "Id42APIStack", props, network, database);
        var botApp = new BotApplicationStack(app, "Id42BotAppStack", props, database);
        var botEnv = new BotEnvironmentStack(app, "Id42BotEnvironmentStack", props,  network, database, botApp);
        var bastion = new BastionStack(app, "Id42BastionStack", props, network);
        //new Id42Stack(app, "Id42CdkStack", props, network, database,api);
        app.synth();
    }
}

