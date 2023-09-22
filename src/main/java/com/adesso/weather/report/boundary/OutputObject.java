package com.adesso.weather.report.boundary;

import com.adesso.weather.report.entity.CityReport;

public class OutputObject {

    private String requestId;

    private CityReport cityReport;

    private String message;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    public CityReport getCityReport() {
        return cityReport;
    }

    public void setCityReport(final CityReport cityReport) {
        this.cityReport = cityReport;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
