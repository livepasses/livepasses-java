package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Resolve a scanned barcode or NFC tap value and redeem it in one call, so a scanner does not
 * need a separate lookup round-trip first.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedeemByScanParams {

    private final String scannedValue;
    private final String redemptionMethod;
    private final String redemptionChannel;
    private final Double latitude;
    private final Double longitude;

    private RedeemByScanParams(Builder builder) {
        this.scannedValue = builder.scannedValue;
        this.redemptionMethod = builder.redemptionMethod;
        this.redemptionChannel = builder.redemptionChannel;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public static Builder builder() { return new Builder(); }

    public String getScannedValue() { return scannedValue; }
    public String getRedemptionMethod() { return redemptionMethod; }
    public String getRedemptionChannel() { return redemptionChannel; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }

    public static class Builder {
        private String scannedValue;
        private String redemptionMethod;
        private String redemptionChannel;
        private Double latitude;
        private Double longitude;

        public Builder scannedValue(String v) { this.scannedValue = v; return this; }
        public Builder redemptionMethod(String v) { this.redemptionMethod = v; return this; }
        public Builder redemptionChannel(String v) { this.redemptionChannel = v; return this; }
        public Builder latitude(Double v) { this.latitude = v; return this; }
        public Builder longitude(Double v) { this.longitude = v; return this; }

        public RedeemByScanParams build() { return new RedeemByScanParams(this); }
    }
}
