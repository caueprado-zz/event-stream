package com.event.handler;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.event.request.PersonRequest;
import com.event.request.PersonResponse;

public class SavePersonHandler implements RequestHandler<PersonRequest, PersonResponse> {

    private DynamoDB dynamoDb;
    private static final String TABLE = "Person";
    private static final Regions REGION = Regions.DEFAULT_REGION;

    public SavePersonHandler(DynamoDB dynamoDB) {
        this.dynamoDb = dynamoDB;
    }

    public PersonResponse handleRequest(final PersonRequest personRequest, Context context) {
        this.initDynamoDbClient();
        persistData(personRequest);
        return new PersonResponse("Success on saving");
    }

    private PutItemOutcome persistData(PersonRequest personRequest) throws ConditionalCheckFailedException {
        return this.dynamoDb.getTable(TABLE)
                .putItem(new PutItemSpec().withItem(new Item()
                                .withString("name", personRequest.getName())));
    }

    private void initDynamoDbClient() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}
