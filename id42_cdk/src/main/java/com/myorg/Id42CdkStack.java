package com.myorg;

import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.s3.Bucket;
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
        var certificate = Certificate.fromCertificateArn(this, "WebAppCertificate", DEFAULT_CERTIFICANTE_ARN);


        var bucket = Bucket.Builder.create(this, "WebAppBucket")
            .build();

        var allowPublic = PolicyStatement.Builder.create()
                .actions(List.of("s3:GetObject"))
                .resources(List.of(bucket.getBucketArn() + "/*"))
                .build();

        bucket.addToResourcePolicy(allowPublic);

        var webappOrigin = S3Origin.Builder.create(bucket)
            .build();
        var defaultBehavior = BehaviorOptions.builder()
                .origin(webappOrigin)
            .build();

        var distro = Distribution.Builder.create(this, "WebAppDistro")
            .defaultBehavior(defaultBehavior)
                .certificate(certificate)
                .domainNames(domainNames)
            .build();
    
    }
}
