package com.pg.s3snssqs;

import com.myorg.HitCounter;
import com.myorg.HitCounterProps;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.*;
import software.amazon.awscdk.services.s3.notifications.SnsDestination;
import software.amazon.awscdk.services.sns.*;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.IQueue;
import software.amazon.awscdk.services.sqs.Queue;
/*
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;*/

public class S3SnsSqsStack extends Stack {
    public S3SnsSqsStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public S3SnsSqsStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        Bucket bucket =  Bucket.Builder.create(this,"graph-pk-bkt-test")
                .bucketName("graph-loader-pk-bkt-test")
                .build();

        ITopic topic = Topic.Builder.create(this,"graph-pk-topic-test")
                .topicName("graph-loader-pk-topic-test")
                 .build();

        Queue queue = Queue.Builder.create(this,"graph-loader-pk-queue-test")
                .queueName("graph-loader-pk-queue-test")
                .build();

        NotificationKeyFilter.Builder notificationFilter =  NotificationKeyFilter.builder();

        NotificationKeyFilter notificationKeyFilter = notificationFilter.suffix("*.parquet").build();

        bucket.addEventNotification(EventType.OBJECT_CREATED, new SnsDestination(topic) ,notificationKeyFilter);

       topic.addSubscription(new SqsSubscription(queue));

        CfnOutputProps cfnOutputProps = CfnOutputProps.builder().value(topic.getTopicArn()).description("topic arn valuess").build();


        CfnOutput CfnOutput =   new CfnOutput(this,"snsTopicArn",cfnOutputProps);

/*
        SubscriptionProps subProps = SubscriptionProps.builder().deadLetterQueue(queue).build();

        ITopicSubscription subscription = new Subscription(this,"my-subscription",subProps);

        topic.addSubscription(subscription);*/

    }
}
