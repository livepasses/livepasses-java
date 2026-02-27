package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassPlatform {

    private final boolean available;
    private final String addToWalletUrl;
    private final String passUrl;
    private final String jwtToken;
    private final List<String> features;

    @JsonCreator
    public PassPlatform(
            @JsonProperty("available") boolean available,
            @JsonProperty("addToWalletUrl") String addToWalletUrl,
            @JsonProperty("passUrl") String passUrl,
            @JsonProperty("jwtToken") String jwtToken,
            @JsonProperty("features") List<String> features) {
        this.available = available;
        this.addToWalletUrl = addToWalletUrl;
        this.passUrl = passUrl;
        this.jwtToken = jwtToken;
        this.features = features != null ? features : Collections.emptyList();
    }

    public boolean isAvailable() { return available; }
    public String getAddToWalletUrl() { return addToWalletUrl; }
    public String getPassUrl() { return passUrl; }
    public String getJwtToken() { return jwtToken; }
    public List<String> getFeatures() { return features; }
}
