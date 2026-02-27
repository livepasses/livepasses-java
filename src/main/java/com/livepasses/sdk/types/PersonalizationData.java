package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalizationData {

    private final String dietaryRestrictions;
    private final String accessibilityNeeds;
    private final Map<String, String> customFields;

    private PersonalizationData(Builder builder) {
        this.dietaryRestrictions = builder.dietaryRestrictions;
        this.accessibilityNeeds = builder.accessibilityNeeds;
        this.customFields = builder.customFields;
    }

    public static Builder builder() { return new Builder(); }

    public String getDietaryRestrictions() { return dietaryRestrictions; }
    public String getAccessibilityNeeds() { return accessibilityNeeds; }
    public Map<String, String> getCustomFields() { return customFields; }

    public static class Builder {
        private String dietaryRestrictions;
        private String accessibilityNeeds;
        private Map<String, String> customFields;

        public Builder dietaryRestrictions(String v) { this.dietaryRestrictions = v; return this; }
        public Builder accessibilityNeeds(String v) { this.accessibilityNeeds = v; return this; }
        public Builder customFields(Map<String, String> v) { this.customFields = v; return this; }

        public PersonalizationData build() { return new PersonalizationData(this); }
    }
}
