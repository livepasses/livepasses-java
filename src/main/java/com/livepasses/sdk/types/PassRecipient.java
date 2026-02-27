package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassRecipient {

    private final CustomerInfo customer;
    private final BusinessData businessData;
    private final PersonalizationData personalizations;

    private PassRecipient(Builder builder) {
        this.customer = builder.customer;
        this.businessData = builder.businessData;
        this.personalizations = builder.personalizations;
    }

    public static Builder builder() { return new Builder(); }

    public CustomerInfo getCustomer() { return customer; }
    public BusinessData getBusinessData() { return businessData; }
    public PersonalizationData getPersonalizations() { return personalizations; }

    public static class Builder {
        private CustomerInfo customer;
        private BusinessData businessData;
        private PersonalizationData personalizations;

        public Builder customer(CustomerInfo v) { this.customer = v; return this; }
        public Builder businessData(BusinessData v) { this.businessData = v; return this; }
        public Builder personalizations(PersonalizationData v) { this.personalizations = v; return this; }

        public PassRecipient build() {
            if (customer == null) throw new IllegalArgumentException("customer is required");
            if (businessData == null) throw new IllegalArgumentException("businessData is required");
            return new PassRecipient(this);
        }
    }
}
