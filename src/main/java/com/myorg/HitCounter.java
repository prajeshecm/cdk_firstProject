package com.myorg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

import java.util.HashMap;
import java.util.Map;

public class HitCounter  extends Construct {

    private static final Logger logger =   LoggerFactory.getLogger(HitCounter.class);

    private Table dynamoTable ;
    private Function handler  ;

    public HitCounter(software.constructs.@NotNull Construct scope, @NotNull String id) {
        super(scope, id);
    }


    public HitCounter(software.constructs.@NotNull Construct scope, @NotNull String id,@NotNull HitCounterProps counterProps) {
        super(scope, id);


        //Dynamo table where we will be string all the hit count from lambda.
         dynamoTable =  Table.Builder.create(this,"HIT_COUNT")
                .partitionKey(Attribute.builder()
                    .name("path")
                    .type(AttributeType.STRING)
                    .build())
                .build();


        //as lambda is needed with couple of env variables.
        final Map<String , String> envVariables = new HashMap<>();
        envVariables.put("HITS_TABLE_NAME",dynamoTable.getTableName());
        envVariables.put("DOWNSTREAM_FUNCTION_NAME",counterProps.getUrlCount().getFunctionName());

        //Lambda function which will save the data_hit to the dynao table.
        handler =   Function.Builder.create(this,"HitCounterHandler")
                .runtime(Runtime.NODEJS_14_X)
                .description("lambda which saves the hit in dynamoDB")
                .handler("hitcounter.handler")
                .code(Code.fromAsset("lambda"))
                .environment(envVariables)
                .build();

        // Grants the lambda function read/write permissions to our table
        dynamoTable.grantReadWriteData(this.handler);

        // Grants the lambda function invoke permissions to the downstream function
        counterProps.getUrlCount().grantInvoke(this.handler);
    }

    public Table getdynamoTable(){
        return dynamoTable ;
    }



    public Function getHandler(){
        return handler ;
    }
}
