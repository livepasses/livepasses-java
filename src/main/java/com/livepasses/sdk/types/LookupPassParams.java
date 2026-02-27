package com.livepasses.sdk.types;

import java.util.LinkedHashMap;
import java.util.Map;

public class LookupPassParams {

    private final String passId;
    private final String passNumber;

    private LookupPassParams(Builder builder) {
        this.passId = builder.passId;
        this.passNumber = builder.passNumber;
    }

    public static Builder builder() { return new Builder(); }

    public String getPassId() { return passId; }
    public String getPassNumber() { return passNumber; }

    public Map<String, String> toQueryParams() {
        Map<String, String> params = new LinkedHashMap<>();
        if (passId != null) params.put("passId", passId);
        if (passNumber != null) params.put("passNumber", passNumber);
        return params;
    }

    public static class Builder {
        private String passId;
        private String passNumber;

        public Builder passId(String v) { this.passId = v; return this; }
        public Builder passNumber(String v) { this.passNumber = v; return this; }

        public LookupPassParams build() { return new LookupPassParams(this); }
    }
}
