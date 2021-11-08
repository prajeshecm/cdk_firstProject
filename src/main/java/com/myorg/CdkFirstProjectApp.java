package com.myorg;

import software.amazon.awscdk.core.App;

public final class CdkFirstProjectApp {
    public static void main(final String[] args) {
        App app = new App();

        new CdkFirstProjectStack(app, "CdkFirstProjectStack");

        app.synth();
    }
}
