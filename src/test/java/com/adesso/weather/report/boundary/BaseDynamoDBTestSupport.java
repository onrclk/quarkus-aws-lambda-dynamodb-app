package com.adesso.weather.report.boundary;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseDynamoDBTestSupport {

    @ConfigProperty(name = "DDB_TABLE")
    String ddbTable;

    @Inject
    protected DynamoDbClient dynamoDbClient;

    @BeforeAll
    void createTable() {
        if (dynamoDbClient.listTables().tableNames().contains(ddbTable)) {
            return;
        }

        final CreateTableRequest createTableRequest = CreateTableRequest.builder()
                                                                        .tableName(ddbTable)
                                                                        .keySchema(KeySchemaElement.builder()
                                                                                                   .attributeName("id")
                                                                                                   .keyType(KeyType.HASH)
                                                                                                   .build())
                                                                        .attributeDefinitions(AttributeDefinition.builder()
                                                                                                                 .attributeName(
                                                                                                                     "id")
                                                                                                                 .attributeType(
                                                                                                                     ScalarAttributeType.N)
                                                                                                                 .build())
                                                                        .provisionedThroughput(ProvisionedThroughput.builder()
                                                                                                                    .readCapacityUnits(
                                                                                                                        1L)
                                                                                                                    .writeCapacityUnits(
                                                                                                                        1L)
                                                                                                                    .build())
                                                                        .build();
        dynamoDbClient.createTable(createTableRequest);
    }
}
