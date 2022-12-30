package id42.cdk;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class Id42CdkApp {
    public static void main(String[] args) throws Exception {
        new CDKStacks().run(args);
    }
}

