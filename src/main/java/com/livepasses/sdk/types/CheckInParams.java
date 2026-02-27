package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckInParams {

    private final RedemptionLocation location;
    private final String notes;

    private CheckInParams(Builder builder) {
        this.location = builder.location;
        this.notes = builder.notes;
    }

    public static Builder builder() { return new Builder(); }

    public RedemptionLocation getLocation() { return location; }
    public String getNotes() { return notes; }

    public static class Builder {
        private RedemptionLocation location;
        private String notes;

        public Builder location(RedemptionLocation v) { this.location = v; return this; }
        public Builder notes(String v) { this.notes = v; return this; }

        public CheckInParams build() { return new CheckInParams(this); }
    }
}
