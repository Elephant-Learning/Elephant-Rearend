package me.elephantsuite.timeline.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.timeline.TimelineVisibility;
import me.elephantsuite.timeline.event.Importance;

public class TimelineRequest {

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class CreateTimeline {
        private final String name;

        private final String description;

        private final TimelineVisibility timelineVisibility;

        private final long userId;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class RenameTimeline {
        private final String name;

        private final long timelineId;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetVisibility {
        private final TimelineVisibility visibility;

        private final long timelineId;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetLikes {
        private final int likes;

        private final long timelineId;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetEventDate {
        private final long eventId;

        private final LocalDateTime date;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetEventName {
        private final long eventId;

        private final String name;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetEventImportance {
        private final long eventId;

        private final Importance importance;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetMarkerName {
        private final long markerId;

        private final String name;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class SetMarkerDate {
        private final long markerId;

        private final LocalDateTime localDateTime;
    }


    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class CreateEvent {
        private final String name;

        private final LocalDateTime date;

        private final LocalDateTime endDate;

        private final String description;

        private final Importance importance;

        private final long timelineId;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class CreateMarker {
        private final String name;

        private final LocalDateTime date;

        private final long timelineId;
    }
}
