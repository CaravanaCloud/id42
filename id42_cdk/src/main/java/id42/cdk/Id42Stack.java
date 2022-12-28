package id42.cdk;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

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
    }
}
