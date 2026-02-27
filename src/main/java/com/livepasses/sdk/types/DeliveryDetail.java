package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryDetail {

    private final String recipient;
    private final String status;

    @JsonCreator
    public DeliveryDetail(
            @JsonProperty("recipient") String recipient,
            @JsonProperty("status") String status) {
        this.recipient = recipient;
        this.status = status;
    }

    public String getRecipient() { return recipient; }
    public String getStatus() { return status; }
}
