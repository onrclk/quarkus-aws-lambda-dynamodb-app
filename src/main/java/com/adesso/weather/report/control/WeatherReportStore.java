package com.adesso.weather.report.control;

import com.adesso.weather.report.boundary.InputObject;
import com.adesso.weather.report.boundary.OutputObject;
import com.adesso.weather.report.entity.CityReport;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@ApplicationScoped
public class WeatherReportStore {

    public static final String CITY_MUST_BE_CHOOSEN = "City Id must be given";

    @ConfigProperty(name = "DDB_TABLE")
    String DDB_TABLE;
    @Inject
    DynamoDbClient dynamoDbClient;

    DynamoDbEnhancedClient enhancedClient;

    DynamoDbTable<CityReport> cityReportTable;

    @PostConstruct
    void init() {
        enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
        cityReportTable = enhancedClient.table(DDB_TABLE, TableSchema.fromClass(CityReport.class));
    }

    public OutputObject getCityWeatherReport(final InputObject input) {

        if (input.getCityId() <= 0) {
            throw new IllegalArgumentException(CITY_MUST_BE_CHOOSEN);
        }

        final CityReport cityReport = cityReportTable.getItem(Key.builder().partitionValue(input.getCityId()).build());
        OutputObject out = new OutputObject();
        out.setCityReport(cityReport);
        return out;
    }
}
