package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneratePassesParams {

    private final String templateId;
    private final List<PassRecipient> passes;
    private final BusinessContext businessContext;
    private final PassGenerationOptions options;

    private GeneratePassesParams(Builder builder) {
        this.templateId = builder.templateId;
        this.passes = builder.passes;
        this.businessContext = builder.businessContext;
        this.options = builder.options;
    }

    public static Builder builder() { return new Builder(); }

    public String getTemplateId() { return templateId; }
    public List<PassRecipient> getPasses() { return passes; }
    public BusinessContext getBusinessContext() { return businessContext; }
    public PassGenerationOptions getOptions() { return options; }

    public static class Builder {
        private String templateId;
        private List<PassRecipient> passes;
        private BusinessContext businessContext;
        private PassGenerationOptions options;

        public Builder templateId(String v) { this.templateId = v; return this; }
        public Builder passes(List<PassRecipient> v) { this.passes = v; return this; }
        public Builder businessContext(BusinessContext v) { this.businessContext = v; return this; }
        public Builder options(PassGenerationOptions v) { this.options = v; return this; }

        public GeneratePassesParams build() {
            if (templateId == null) throw new IllegalArgumentException("templateId is required");
            if (passes == null || passes.isEmpty()) throw new IllegalArgumentException("passes is required");
            return new GeneratePassesParams(this);
        }
    }
}
