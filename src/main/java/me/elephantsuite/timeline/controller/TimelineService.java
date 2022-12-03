package me.elephantsuite.timeline.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.timeline.Timeline;
import me.elephantsuite.timeline.TimelineRepositoryService;
import me.elephantsuite.timeline.TimelineVisibility;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TimelineService {

    private final ElephantUserService userService;

    private final TimelineRepositoryService timelineRepositoryService;

    public Response createTimeline(TimelineRequest.CreateTimeline request) {
        long userId = request.getUserId();
        String name = request.getName();
        TimelineVisibility visibility = request.getTimelineVisibility();
        String details = request.getDescription();

        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        Timeline timeline = new Timeline(user, name, visibility, details);
        user.getTimelines().add(timeline);

        timeline = timelineRepositoryService.save(timeline);

        user = userService.saveUser(user);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Created Timeline!")
                .addObject("timeline", timeline)
                .build();
    }

    public Response deleteTimeline(long id) {
        Timeline timeline = timelineRepositoryService.getTimelineById(id);

        if (timeline == null) {
            throw new InvalidIdException(id, InvalidIdType.TIMELINE);
        }

        timelineRepositoryService.deleteTimeline(timeline);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Deleted Timeline!")
                .build();
    }

    public Response renameTimeline(TimelineRequest.RenameTimeline request) {
        long timelineId = request.getTimelineId();
        String name = request.getName();

        Timeline timeline = timelineRepositoryService.getTimelineById(timelineId);

        if (timeline == null) {
            throw new InvalidIdException(timelineId, InvalidIdType.TIMELINE);
        }

        timeline.setName(name);

        return null;
    }
}
