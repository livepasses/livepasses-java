package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushTemplatePassesParams {

    private final Map<String, Object> updatedFields;
    private final String reason;

    private PushTemplatePassesParams(Builder builder) {
        this.updatedFields = builder.updatedFields;
        this.reason = builder.reason;
    }

    public static Builder builder() { return new Builder(); }

    public Map<String, Object> getUpdatedFields() { return updatedFields; }
    public String getReason() { return reason; }

    public static class Builder {
        private Map<String, Object> updatedFields;
        private String reason;

        public Builder updatedFields(Map<String, Object> v) { this.updatedFields = v; return this; }
        public Builder reason(String v) { this.reason = v; return this; }

        public PushTemplatePassesParams build() {
            if (updatedFields == null || updatedFields.isEmpty())
                throw new IllegalArgumentException("updatedFields is required");
            return new PushTemplatePassesParams(this);
        }
    }
}
