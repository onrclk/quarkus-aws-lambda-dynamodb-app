package com.adesso.weather.report.entity;

import java.time.Instant;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class WeatherReportItem {

    private Instant time;
    private double temperature;
    private String description;

    @DynamoDbAttribute("time")
    public Instant getTime() {
        return time;
    }

    public void setTime(final Instant time) {
        this.time = time;
    }

    @DynamoDbAttribute("temperature")
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(final double temperature) {
        this.temperature = temperature;
    }

    @DynamoDbAttribute("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
