package com.pg.s3snssqs;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.EventType;
import software.amazon.awscdk.services.s3.NotificationKeyFilter;
import software.amazon.awscdk.services.s3.notifications.SnsDestination;
import software.amazon.awscdk.services.sns.ITopic;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;

import java.security.Principal;

public class S3SnsSqsStack extends Stack {
    public S3SnsSqsStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public S3SnsSqsStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        //create bucket.
        Bucket bucket = Bucket.Builder.create(this, "graph-pk-bkt-test")
                .bucketName("graph-loader-pk-bkt-test")
                .build();

        //create topic.
        ITopic topic = Topic.Builder.create(this, "graph-pk-topic-test")
                .topicName("graph-loader-pk-topic-test")
                .build();

        //create queue.
        Queue queue = Queue.Builder.create(this, "graph-loader-pk-queue-test")
                .queueName("graph-loader-pk-queue-test")
                .build();

        //add filter to the bucket.
        NotificationKeyFilter.Builder notificationFilter = NotificationKeyFilter.builder();
        NotificationKeyFilter notificationKeyFilter = notificationFilter.suffix("*.parquet").build();
        bucket.addEventNotification(EventType.OBJECT_CREATED, new SnsDestination(topic), notificationKeyFilter);

        //Subscribe sqs to topic
        topic.addSubscription(new SqsSubscription(queue));

        //Cout the stack topic arn.
        CfnOutputProps cfnOutputProps = CfnOutputProps.builder().value(topic.getTopicArn()).description("topic arn valuess").build();
        CfnOutput CfnOutput = new CfnOutput(this, "snsTopicArn", cfnOutputProps);

  /* please validate the below access policy for SNS       {
  “Version”: “2008-10-17",
  “Id”: “__default_policy_ID”,
  “Statement”: [
            {
      “Sid”: “__default_statement_ID”,
      “Effect”: “Allow”,
      “Principal”: {
        “AWS”: “*”
            },
      “Action”: [
        “SNS:GetTopicAttributes”,
        “SNS:SetTopicAttributes”,
        “SNS:AddPermission”,
        “SNS:RemovePermission”,
        “SNS:DeleteTopic”,
        “SNS:Subscribe”,
        “SNS:ListSubscriptionsByTopic”,
        “SNS:Publish”,
        “SNS:Receive”
      ],
      “Resource”: “arn:aws:sns:us-east-1:716915692812:loaders3bucket”,
      “Condition”: {
        “ArnLike”: {
          “AWS:SourceArn”: “arn:aws:s3:::loaders3bucket”
                }
            }
            }
  ]
        }*/
    }
}
