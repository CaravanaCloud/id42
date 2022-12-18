package id42;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.cloudfront.BehaviorOptions;
import software.amazon.awscdk.services.cloudfront.Distribution;
import software.amazon.awscdk.services.cloudfront.origins.S3Origin;
import software.amazon.awscdk.services.iam.AnyPrincipal;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.List;

public class APIStack extends Stack {
    private Distribution distribution;

    public APIStack(final Construct scope, final String id) {
        this(scope, id, null, null, null);
    }

    public APIStack(final Construct scope, 
                    final String id,
                    final StackProps props,
                    final NetworkStack network,
                    final DatabaseStack database) {
        super(scope, id, props);
        var domainNames = StackConfig.domainNames.getList();

        var certificate = Certificate.fromCertificateArn(this, "WebAppCertificate", StackConfig.certificateARN.getString());

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
        this.distribution = Distribution.Builder.create(this, "WebAppDistro")
                .defaultBehavior(defaultBehavior)
                .certificate(certificate)
                .domainNames(domainNames)
                .defaultRootObject("index.html")
                .build();
    }

    public Distribution distribution() {
        return distribution;
    }
}
