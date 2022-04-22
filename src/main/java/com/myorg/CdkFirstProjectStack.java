package com.myorg;

import software.amazon.awscdk.core.Construct;

import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.IFunction;
import software.amazon.awscdk.services.lambda.Runtime;

import software.amazon.awscdk.services.apigateway.RestApi;
/*
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;*/

public class CdkFirstProjectStack extends Stack {
    public CdkFirstProjectStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkFirstProjectStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        /*
        scenario 1 : where we need the stack to get the SQS and Topic added to that.

        final Queue queue = Queue.Builder.create(this, "CdkFirstProjectQueue")
                .visibilityTimeout(Duration.seconds(300))
                .build();

        final Topic topic = Topic.Builder.create(this, "CdkFirstProjectTopic")
            .displayName("My First Topic Yeah")
            .build();

        topic.addSubscription(new SqsSubscription(queue));

        End of scenario 1 :
        */

        /*
        scenario 1 : where we need the stack to get REST API to call the lambda function , which in turn return " hello CDK ".


        final Function helloFunction = Function.Builder.create(this,"HelloHandler")
                .runtime(Runtime.NODEJS_14_X) // execution env.
                .code(Code.fromAsset("lambda"))  //code will load from "lambda" directory
                .handler("hello.handler") //file is hello and function is handler
                .build();


        LambdaRestApi.Builder.create(this,"HelloHandlerRestApi")
                .restApiName("HelloHandlerRestApi")
                .description("use to get a response from lambda")
                .handler(helloFunction)
                .build();


                 End of scenario 2 :
         */


        //Scenario 3 : get the stack , where the lamda store the no of URL hits in dynamoDB.

       Function hello =  Function.Builder.create(this,"HelloHandler")
                .runtime(Runtime.NODEJS_14_X)    // execution environment
                .code(Code.fromAsset("lambda"))  // code loaded from the "lambda" directory
                .handler("hello.handler")
                .build();

        final HitCounter hitCounter = new HitCounter(this, "HelloHitCounter", HitCounterProps.builder().getUrlCount(hello).build());

        LambdaRestApi.Builder.create(this,"Endpoint")
                .restApiName("HelloHandlerRestApi")
                .description("use to get a response from lambda")
                .handler(hitCounter.getHandler())
                .build();

    }
}
