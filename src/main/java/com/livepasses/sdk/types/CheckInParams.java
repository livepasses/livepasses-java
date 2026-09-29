package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Body of {@code POST /api/passes/{passId}/check-in}.
 *
 * <p>To attach free-form context to a check-in, use {@link Builder#metadata(Map)}; the API
 * refuses any field it does not declare.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckInParams {

    private final RedemptionLocation location;
    private final Map<String, String> metadata;

    private CheckInParams(Builder builder) {
        this.location = builder.location;
        this.metadata = builder.metadata == null
                ? null
                : Collections.unmodifiableMap(new LinkedHashMap<>(builder.metadata));
    }

    public static Builder builder() { return new Builder(); }

    public RedemptionLocation getLocation() { return location; }
    public Map<String, String> getMetadata() { return metadata; }

    public static class Builder {
        private RedemptionLocation location;
        private Map<String, String> metadata;

        public Builder location(RedemptionLocation v) { this.location = v; return this; }

        /** Free-form key/value context recorded with the check-in. */
        public Builder metadata(Map<String, String> v) { this.metadata = v; return this; }

        public CheckInParams build() { return new CheckInParams(this); }
    }
}
