package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkUpdatePassesParams {

    private final List<String> passIds;
    private final BusinessData businessData;
    private final BusinessContext businessContext;

    private BulkUpdatePassesParams(Builder builder) {
        this.passIds = builder.passIds;
        this.businessData = builder.businessData;
        this.businessContext = builder.businessContext;
    }

    public static Builder builder() { return new Builder(); }

    public List<String> getPassIds() { return passIds; }
    public BusinessData getBusinessData() { return businessData; }
    public BusinessContext getBusinessContext() { return businessContext; }

    public static class Builder {
        private List<String> passIds;
        private BusinessData businessData;
        private BusinessContext businessContext;

        public Builder passIds(List<String> v) { this.passIds = v; return this; }
        public Builder businessData(BusinessData v) { this.businessData = v; return this; }
        public Builder businessContext(BusinessContext v) { this.businessContext = v; return this; }

        public BulkUpdatePassesParams build() {
            if (passIds == null || passIds.isEmpty()) throw new IllegalArgumentException("passIds is required");
            return new BulkUpdatePassesParams(this);
        }
    }
}
