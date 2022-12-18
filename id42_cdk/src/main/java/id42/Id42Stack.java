package id42;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.SecretValue;
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

public class Id42Stack extends Stack {

    public Id42Stack(final Construct scope, final String id) {
        this(scope, id, null, null, null, null);
    }

    public Id42Stack(final Construct scope,
                     final String id,
                     final StackProps props,
                     final NetworkStack network,
                     final DatabaseStack database,
                     final APIStack api) {
        super(scope, id, props);
        //TODO: Load from context https://docs.aws.amazon.com/cdk/v2/guide/context.html

        // Rede
        var vpc = network.vpc();
        var privateNets = network.privateSubnets();


        // Web

        var distroId = CfnOutput.Builder.create(this, "WebAppDistroId")
                .value(api.distribution().getDistributionId())
                .build();
    }
}
