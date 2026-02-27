package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerInfo {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String preferredLanguage;

    private CustomerInfo(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phone = builder.phone;
        this.preferredLanguage = builder.preferredLanguage;
    }

    public static Builder builder() { return new Builder(); }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPreferredLanguage() { return preferredLanguage; }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String preferredLanguage;

        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder preferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; return this; }

        public CustomerInfo build() {
            if (firstName == null) throw new IllegalArgumentException("firstName is required");
            if (lastName == null) throw new IllegalArgumentException("lastName is required");
            return new CustomerInfo(this);
        }
    }
}
