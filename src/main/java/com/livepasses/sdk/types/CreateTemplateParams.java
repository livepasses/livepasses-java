package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTemplateParams {

    private final String name;
    private final String description;
    private final Map<String, Object> businessFeatures;
    private final List<String> requiredMedia;

    private CreateTemplateParams(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.businessFeatures = builder.businessFeatures;
        this.requiredMedia = builder.requiredMedia;
    }

    public static Builder builder() { return new Builder(); }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Object> getBusinessFeatures() { return businessFeatures; }
    public List<String> getRequiredMedia() { return requiredMedia; }

    public static class Builder {
        private String name;
        private String description;
        private Map<String, Object> businessFeatures;
        private List<String> requiredMedia;

        public Builder name(String v) { this.name = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder businessFeatures(Map<String, Object> v) { this.businessFeatures = v; return this; }
        public Builder requiredMedia(List<String> v) { this.requiredMedia = v; return this; }

        public CreateTemplateParams build() {
            if (name == null) throw new IllegalArgumentException("name is required");
            if (businessFeatures == null) throw new IllegalArgumentException("businessFeatures is required");
            return new CreateTemplateParams(this);
        }
    }
}
