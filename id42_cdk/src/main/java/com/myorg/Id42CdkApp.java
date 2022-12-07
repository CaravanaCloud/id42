package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class Id42CdkApp {
    public static void main(final String[] args) {
        App app = new App();
        new Id42CdkStack(app, "Id42CdkStack", StackProps.builder()
                .build());
        app.synth();
    }
}

