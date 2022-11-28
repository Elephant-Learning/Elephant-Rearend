package me.elephantsuite.timeline.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.timeline.TimelineVisibility;

public class TimelineRequest {

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class CreateTimeline {
        private final String name;

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
}
