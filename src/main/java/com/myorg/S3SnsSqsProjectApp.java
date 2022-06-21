package com.pg.s3snssqs;

import com.myorg.CdkFirstProjectStack;
import software.amazon.awscdk.core.App;

public final class S3SnsSqsProjectApp {
    public static void main(final String[] args) {
        App app = new App();

        new S3SnsSqsStack(app, "S3SnsSqsStack");

        app.synth();
    }
}
