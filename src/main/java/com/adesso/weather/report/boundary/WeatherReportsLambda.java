package com.adesso.weather.report.boundary;

import com.adesso.weather.report.control.WeatherReportStore;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("weatherReportLambda")
public class WeatherReportsLambda implements RequestHandler<InputObject, OutputObject> {

    @Inject
    WeatherReportStore reportStore;

    @Override
    public OutputObject handleRequest(InputObject input, Context context) {
        final var output = reportStore.getCityWeatherReport(input);
        output.setRequestId(context.getAwsRequestId());
        return output;
    }
}
