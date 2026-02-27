package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassGenerationOptions {

    private final String deliveryMethod;
    private final Boolean generateForAllPlatforms;

    private PassGenerationOptions(Builder builder) {
        this.deliveryMethod = builder.deliveryMethod;
        this.generateForAllPlatforms = builder.generateForAllPlatforms;
    }

    public static Builder builder() { return new Builder(); }

    public String getDeliveryMethod() { return deliveryMethod; }
    public Boolean getGenerateForAllPlatforms() { return generateForAllPlatforms; }

    public static class Builder {
        private String deliveryMethod;
        private Boolean generateForAllPlatforms;

        public Builder deliveryMethod(String v) { this.deliveryMethod = v; return this; }
        public Builder generateForAllPlatforms(Boolean v) { this.generateForAllPlatforms = v; return this; }

        public PassGenerationOptions build() { return new PassGenerationOptions(this); }
    }
}
