package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePassParams {

    private final BusinessData businessData;
    private final BusinessContext businessContext;

    private UpdatePassParams(Builder builder) {
        this.businessData = builder.businessData;
        this.businessContext = builder.businessContext;
    }

    public static Builder builder() { return new Builder(); }

    public BusinessData getBusinessData() { return businessData; }
    public BusinessContext getBusinessContext() { return businessContext; }

    public static class Builder {
        private BusinessData businessData;
        private BusinessContext businessContext;

        public Builder businessData(BusinessData v) { this.businessData = v; return this; }
        public Builder businessContext(BusinessContext v) { this.businessContext = v; return this; }

        public UpdatePassParams build() { return new UpdatePassParams(this); }
    }
}
