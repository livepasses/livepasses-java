package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassPlatforms {

    private final PassPlatform apple;
    private final PassPlatform google;

    @JsonCreator
    public PassPlatforms(
            @JsonProperty("apple") PassPlatform apple,
            @JsonProperty("google") PassPlatform google) {
        this.apple = apple;
        this.google = google;
    }

    public PassPlatform getApple() { return apple; }
    public PassPlatform getGoogle() { return google; }
}
