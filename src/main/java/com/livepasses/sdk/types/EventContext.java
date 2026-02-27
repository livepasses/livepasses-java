package com.livepasses.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventContext {

    private final String eventName;
    private final String eventDate;
    private final String doorsOpen;
    private final String specialAnnouncement;

    private EventContext(Builder builder) {
        this.eventName = builder.eventName;
        this.eventDate = builder.eventDate;
        this.doorsOpen = builder.doorsOpen;
        this.specialAnnouncement = builder.specialAnnouncement;
    }

    public static Builder builder() { return new Builder(); }

    public String getEventName() { return eventName; }
    public String getEventDate() { return eventDate; }
    public String getDoorsOpen() { return doorsOpen; }
    public String getSpecialAnnouncement() { return specialAnnouncement; }

    public static class Builder {
        private String eventName;
        private String eventDate;
        private String doorsOpen;
        private String specialAnnouncement;

        public Builder eventName(String v) { this.eventName = v; return this; }
        public Builder eventDate(String v) { this.eventDate = v; return this; }
        public Builder doorsOpen(String v) { this.doorsOpen = v; return this; }
        public Builder specialAnnouncement(String v) { this.specialAnnouncement = v; return this; }

        public EventContext build() { return new EventContext(this); }
    }
}
