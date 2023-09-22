package com.adesso.weather.report.boundary;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.adesso.weather.report.entity.CityReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

@QuarkusTest
class WeatherReportsLambdaIT extends BaseDynamoDBTestSupport {

    @Inject
    ObjectMapper objectMapper;

    DynamoDbEnhancedClient enhancedClient;
    DynamoDbTable<CityReport> cityReportDDBTable;

    @BeforeAll
    void setUp() {
        enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
        cityReportDDBTable = enhancedClient.table(ddbTable, TableSchema.fromClass(CityReport.class));
    }

    @Test
    void testCityReportRetrieved() throws Exception {
        // you test your lambdas by invoking on http://localhost:8081
        // this works in dev mode too

        final CityReport cityReport = saveCityReportItem("cityItemAntalya.json");

        InputObject in = new InputObject();
        in.setCityId(cityReport.getId());
        final OutputObject output = given().contentType("application/json")
                                           .accept("application/json")
                                           .body(in)
                                           .when()
                                           .post()
                                           .then()
                                           .statusCode(200)
                                           .extract()
                                           .as(OutputObject.class);

        MatcherAssert.assertThat(output, notNullValue());
        MatcherAssert.assertThat(output.getRequestId(), notNullValue());
        MatcherAssert.assertThat(output.getCityReport(), equalToObject(cityReport));
    }

    private CityReport saveCityReportItem(final String fileName) throws IOException {
        final String jsonAsText = readFile(fileName);
        CityReport cityReport = objectMapper.readValue(jsonAsText, CityReport.class);

        cityReportDDBTable.putItem(PutItemEnhancedRequest.<CityReport>builder(cityReport.getClass())
                                                         .item(cityReport)
                                                         .build());
        return cityReport;
    }

    private String readFile(final String fileName) throws IOException {
        try (final InputStream fis = getClass().getResourceAsStream(fileName)) {
            assert fis != null;
            return IOUtils.toString(fis, StandardCharsets.UTF_8);
        }
    }

}
