package com.myorg;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.services.apigateway.Deployment;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.iam.AnyPrincipal;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class Id42CdkStack extends Stack {
    static final String DEFAULT_CERTIFICANTE_ARN="arn:aws:acm:us-east-1:746658282231:certificate/cf889a63-7b97-46e8-9c22-e790d064769d";
    public Id42CdkStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public Id42CdkStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        //TODO: Load from context https://docs.aws.amazon.com/cdk/v2/guide/context.html
        var domainNames = List.of("*.id42.cc","id42.cc");

        // Rede
        var subnets = SubnetConfiguration.builder()
                .cidrMask(24)
                .name("id42-subnet")
                .subnetType(SubnetType.PRIVATE_ISOLATED)
                .build();

        var addr = IpAddresses.cidr("10.0.0.0/16");

        var vpc = Vpc.Builder.create(this, "id42-vpc")
                .maxAzs(3)
                .subnetConfiguration(List.of(subnets))
                .ipAddresses(addr)
                .defaultInstanceTenancy(DefaultInstanceTenancy.DEFAULT)
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .build();

        var webSG = SecurityGroup.Builder.create(this, "id42-web-sg")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        //TODO: Restrict this traffic to the correct port
        webSG.addIngressRule(Peer.anyIpv4(), Port.allTcp(), "Allow HTTP from the world");

        var privateNets = SubnetSelection
                .builder()
                .subnets(vpc.getPrivateSubnets())
                .build();

        var subnetGroup = SubnetGroup.Builder.create(this, "id42-subnet-group")
                .vpc(vpc)
                .description("Subnet group for id42")
                .vpcSubnets(privateNets)
                .build();


        var dbSG = SecurityGroup.Builder.create(this, "id42-db-sg")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        dbSG.addIngressRule(Peer.anyIpv4(), Port.tcp(3306), "Allow MySQL IN");

        //TODO: Use parameter store
        var creds = Credentials
                .fromPassword("admin", SecretValue.unsafePlainText("Masterkey123"));

        var auroraProps = AuroraMysqlClusterEngineProps
                .builder()
                .version(AuroraMysqlEngineVersion.VER_2_10_3)
                .build();
        var auroraEngine = DatabaseClusterEngine.auroraMysql(auroraProps);

        var db = ServerlessCluster.Builder.create(this, "id42-db")
                .engine(auroraEngine)
                .vpc(vpc)
                .subnetGroup(subnetGroup)
                .securityGroups(List.of(dbSG))
                .credentials(creds)
                .enableDataApi(true)
                .build();


        var outVpcId = CfnOutput.Builder.create(this, "pbnk-out-vpcId")
                .value(vpc.getVpcId())
                .build();

        // Web
        var certificate = Certificate.fromCertificateArn(this, "WebAppCertificate", DEFAULT_CERTIFICANTE_ARN);

        var bucket = Bucket.Builder.create(this, "WebAppBucket")
            .build();

        var allowPublic = PolicyStatement.Builder.create()
                .actions(List.of("s3:GetObject"))
                .resources(List.of(bucket.getBucketArn() + "/*"))
                .principals(List.of(new AnyPrincipal()))
                .build();
        bucket.addToResourcePolicy(allowPublic);

        var webappSources = List.of(Source.asset("../id42_app/build/web"));
        var deployApp = BucketDeployment.Builder.create(this, "WebAppDeployment")
                .sources(webappSources)
                .destinationBucket(bucket)
                .prune(true)
                .build();

        var webappOrigin = S3Origin.Builder.create(bucket)
                .build();
        var defaultBehavior = BehaviorOptions.builder()
                .origin(webappOrigin)
            .build();
        var distro = Distribution.Builder.create(this, "WebAppDistro")
            .defaultBehavior(defaultBehavior)
                .certificate(certificate)
                .domainNames(domainNames)
                .defaultRootObject("index.html")
            .build();
    
    }
}
