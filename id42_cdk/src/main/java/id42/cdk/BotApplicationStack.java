package id42.cdk;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication;
import software.amazon.awscdk.services.iam.CfnInstanceProfile;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.List;

public class BotApplicationStack extends Stack {
    private final CfnApplication application;
    private final Role role;
    private final CfnInstanceProfile instanceProfile;
    private final Bucket bucket;

    public BotApplicationStack(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public BotApplicationStack(final Construct scope, final String id, final StackProps props, final DatabaseStack database) {
        super(scope, id, props);
        this.application = CfnApplication.Builder.create(this, "id42-bot-application")
                .applicationName("id42-bot")
                .build();

        //TODO: use lest privilege policy
        var adminPolicy = ManagedPolicy.fromManagedPolicyArn(this,
                "AdministratorAccess",
                "arn:aws:iam::aws:policy/AdministratorAccess");

        this.role = Role.Builder.create(this, "id42-bot-eb-role")
                .assumedBy(ServicePrincipal.Builder.create("ec2.amazonaws.com").build())
                .managedPolicies(List.of(adminPolicy))
                .build();

        var stamp = System.currentTimeMillis();
        var instanceProfileName = "id42-eb-iprofile";

        this.instanceProfile = CfnInstanceProfile.Builder.create(this,
                        "id42-eb-iprofile")
                .instanceProfileName(instanceProfileName)
                .roles(List.of(role.getRoleName()))
                .build();

        var jarName ="id42_bot-1.0.0-SNAPSHOT-runner.jar";
        var path = "../id42_bot/target";
        var jar = List.of(Source.asset(path));

        this.bucket = Bucket.Builder.create(this, "BotBucket")
                .build();

        CfnOutput.Builder.create(this, "BotBucketName")
                .value(bucket.getBucketName())
                .build();

    }

    public Bucket bucket() {
        return bucket;
    }

    public CfnApplication application() {
        return application;
    }

    public Role role() {
        return role;
    }

    public CfnInstanceProfile instanceProfile() {
        return instanceProfile;
    }
}
