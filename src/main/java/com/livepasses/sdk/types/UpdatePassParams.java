package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Body of {@code PUT /api/passes/{passId}}.
 *
 * <p>{@code updatedFields} maps updatable field names (for example {@code validUntil},
 * {@code memberTier}, {@code points}) to their new values. {@code messageHeader} and
 * {@code messageBody} set the notification banner shown to the holder; {@code notify(false)}
 * suppresses it. A request must carry at least one updated field or a non-empty
 * {@code messageBody}.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePassParams {

    private final Map<String, Object> updatedFields;
    private final String reason;
    private final String messageHeader;
    private final String messageBody;
    private final Boolean notify;

    private UpdatePassParams(Builder builder) {
        this.updatedFields = builder.updatedFields == null
                ? null
                : Collections.unmodifiableMap(new LinkedHashMap<>(builder.updatedFields));
        this.reason = builder.reason;
        this.messageHeader = builder.messageHeader;
        this.messageBody = builder.messageBody;
        this.notify = builder.notify;
    }

    public static Builder builder() { return new Builder(); }

    public Map<String, Object> getUpdatedFields() { return updatedFields; }
    public String getReason() { return reason; }
    public String getMessageHeader() { return messageHeader; }
    public String getMessageBody() { return messageBody; }
    public Boolean getNotify() { return notify; }

    public static class Builder {
        private Map<String, Object> updatedFields;
        private String reason;
        private String messageHeader;
        private String messageBody;
        private Boolean notify;

        /** Set one field change, e.g. {@code updatedField("memberTier", "Gold")}. */
        public Builder updatedField(String name, Object value) {
            if (updatedFields == null) updatedFields = new LinkedHashMap<>();
            updatedFields.put(name, value);
            return this;
        }

        /** Add every entry of {@code fields} to the field changes. */
        public Builder updatedFields(Map<String, ?> fields) {
            if (updatedFields == null) updatedFields = new LinkedHashMap<>();
            updatedFields.putAll(fields);
            return this;
        }

        /** Why the pass changed (at most 500 characters). */
        public Builder reason(String v) { this.reason = v; return this; }

        /** Banner title shown to the holder (at most 80 characters). */
        public Builder messageHeader(String v) { this.messageHeader = v; return this; }

        /** Banner text shown to the holder (at most 2000 characters). */
        public Builder messageBody(String v) { this.messageBody = v; return this; }

        /** {@code false} suppresses the holder notification; omitted means the API default. */
        public Builder notify(Boolean v) { this.notify = v; return this; }

        public UpdatePassParams build() {
            boolean hasFields = updatedFields != null && !updatedFields.isEmpty();
            boolean hasMessage = messageBody != null && !messageBody.isBlank();
            if (!hasFields && !hasMessage) {
                throw new IllegalArgumentException("updatedFields or messageBody is required");
            }
            return new UpdatePassParams(this);
        }
    }
}
