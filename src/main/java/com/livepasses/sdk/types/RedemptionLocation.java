package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedemptionLocation {

    private final String name;
    private final Double latitude;
    private final Double longitude;

    private RedemptionLocation(Builder builder) {
        this.name = builder.name;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public static Builder builder() { return new Builder(); }

    public String getName() { return name; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public static class Builder {
        private String name;
        private Double latitude;
        private Double longitude;

        public Builder name(String v) { this.name = v; return this; }
        public Builder latitude(Double v) { this.latitude = v; return this; }
        public Builder longitude(Double v) { this.longitude = v; return this; }

        public RedemptionLocation build() { return new RedemptionLocation(this); }
    }
}
