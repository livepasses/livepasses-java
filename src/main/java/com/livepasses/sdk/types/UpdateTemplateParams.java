package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateTemplateParams {

    private final String name;
    private final String description;
    private final Map<String, Object> businessFeatures;

    private UpdateTemplateParams(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.businessFeatures = builder.businessFeatures;
    }

    public static Builder builder() { return new Builder(); }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Object> getBusinessFeatures() { return businessFeatures; }

    public static class Builder {
        private String name;
        private String description;
        private Map<String, Object> businessFeatures;

        public Builder name(String v) { this.name = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder businessFeatures(Map<String, Object> v) { this.businessFeatures = v; return this; }

        public UpdateTemplateParams build() { return new UpdateTemplateParams(this); }
    }
}
