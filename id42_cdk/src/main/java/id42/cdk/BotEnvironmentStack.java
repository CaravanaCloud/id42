package id42.cdk;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationVersion;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationVersionProps;
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.List;
import java.util.stream.Collectors;

public class BotEnvironmentStack extends Stack {
    public BotEnvironmentStack(final Construct scope, final String id) {
        this(scope, id, null, null, null, null, null);
    }

    public BotEnvironmentStack(final Construct scope,
                               final String id,
                               final StackProps props,
                               final NetworkStack network,
                               final DatabaseStack database,
                               final BotApplicationStack botApp,
                               final BotLexStack lex) {
        super(scope, id, props);
        var stamp = System.currentTimeMillis();
        var path = "../id42_bot/target";
        var sources = List.of(Source.asset(path));
        var bucket = botApp.bucket();
        var version = "1.9.0";
        var versionName = "id42_bot-"+version;
        var jarName = versionName+"-runner.jar";

        if(StackConfig.deployToS3.getBoolean()){
            System.out.println("*** Deploying to S3 ***");
            var deployBot = BucketDeployment.Builder.create(this, "id42-bot-s3-deployment")
                        .sources(sources)
                        .destinationBucket(bucket)
                        .prune(true)
                        .build();
        }

        var srcBundle = CfnApplicationVersion.SourceBundleProperty.builder()
                .s3Bucket(botApp.bucket().getBucketName())
                .s3Key(jarName)
                .build();

        var appVersion = CfnApplicationVersion.Builder.create(this, "id42-bot-version")
                .applicationName(botApp.application().getApplicationName())
                .sourceBundle(srcBundle)
                .description(versionName)
                .build();



        var instanceType = CfnEnvironment.OptionSettingProperty.builder()
                .namespace("aws:autoscaling:launchconfiguration")
                .optionName("InstanceType")
                .value(StackConfig.instanceType.getString())
                .build();

        var instanceProfileOpt = option(
                "aws:autoscaling:launchconfiguration",
                "IamInstanceProfile",
                botApp.instanceProfile().getInstanceProfileName());

        var vpc = network.vpc();
        var botSG = SecurityGroup.Builder.create(this, "id42-bot-sg")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        //TODO: Restrict this traffic to the correct port
        botSG.addIngressRule(Peer.anyIpv4(), Port.allTcp(), "Allow bot from the world");

        var webSGOpt = option(
                "aws:autoscaling:launchconfiguration",
                "SecurityGroups",
                botSG.getSecurityGroupId());

        var vpcOpt = option("aws:ec2:vpc",
                "VPCId",
                vpc.getVpcId());

        var subnetIds = vpc.getPublicSubnets()
                .stream()
                .map(s -> s.getSubnetId())
                .collect(Collectors.toList());

        var subnetIdsStr = String.join(",",subnetIds);

        var subnetsOpt = option("aws:ec2:vpc",
                "Subnets",
                subnetIdsStr);

        var publicIpsOpt = option("aws:ec2:vpc",
                "AssociatePublicIpAddress",
                "true");

        var lbType = option("aws:elasticbeanstalk:environment",
                "EnvironmentType",
                "SingleInstance");

        var botUser = option("aws:elasticbeanstalk:application:environment",
                "BOT_USERNAME",
                StackConfig.bot_username.getString());
        //TODO: local env vars not being picked up, move to SMM parameter store anyway
        var botToken = option("aws:elasticbeanstalk:application:environment",
                "BOT_TOKEN",
                StackConfig.bot_token.getString());

        var jdbcUser = option("aws:elasticbeanstalk:application:environment",
                "QUARKUS_DATASOURCE_USERNAME",
                database.getRootUsername());
        var jdbcPass = option("aws:elasticbeanstalk:application:environment",
                "QUARKUS_DATASOURCE_PASSWORD",
                database.getRootPassword());
        var jdbcUrl = option("aws:elasticbeanstalk:application:environment",
                "QUARKUS_DATASOURCE_JDBC_URL",
                database.jdbcUrl());
        var botId = option("aws:elasticbeanstalk:application:environment",
                "BOT_LEX_BOT_ID",
                lex.botId());
        var botAlias = option("aws:elasticbeanstalk:application:environment",
                "BOT_LEX_BOT_ALIAS_ID",
                lex.botAlias());

        var opts = List.of(
                instanceType,
                instanceProfileOpt,
                vpcOpt,
                subnetsOpt,
                publicIpsOpt,
                botUser,
                botToken,
                webSGOpt,
                lbType,
                jdbcUser,
                jdbcPass,
                jdbcUrl,
                botId,
                botAlias);

        var ebEnvName = "id42-eb-env-"+stamp;


        var ebEnv = CfnEnvironment.Builder
                    .create(this, "id42-eb-env")
                    .applicationName(ebEnvName)
                    .applicationName(botApp.application().getApplicationName())
                    .versionLabel(appVersion.getRef())
                    .optionSettings(opts)
                    .solutionStackName("64bit Amazon Linux 2 v3.4.2 running Corretto 17")
                    .build();

            ebEnv.addDependsOn(botApp.application());
            ebEnv.addDependsOn(botApp.instanceProfile());

    }

    private CfnEnvironment.OptionSettingProperty option(String namespace,
                                                        String optionName,
                                                        String value) {
        return CfnEnvironment.OptionSettingProperty.builder()
                .namespace(namespace)
                .optionName(optionName)
                .value(value)
                .build();

    }
}
