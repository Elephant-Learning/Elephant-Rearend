package me.elephantsuite.timeline.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import me.elephantsuite.timeline.event.Event;
import me.elephantsuite.timeline.event.EventRepositoryService;
import me.elephantsuite.timeline.event.Importance;
import me.elephantsuite.timeline.marker.Marker;
import me.elephantsuite.timeline.marker.MarkerRepositoryService;
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

    private final EventRepositoryService eventRepositoryService;

    private final MarkerRepositoryService markerRepositoryService;

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

        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Renamed Timeline")
            .addObject("timeline", timeline)
            .build();
    }

	public Response setVisibility(TimelineRequest.SetVisibility request) {
        long tlId = request.getTimelineId();
        TimelineVisibility visibility = request.getVisibility();

        Timeline timeline = getTimelineById(tlId);

        timeline.setTimelineVisibility(visibility);

        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Visibility!")
            .addObject("timeline", timeline)
            .build();
	}


    public Timeline getTimelineById(long id) {
        Timeline tl = timelineRepositoryService.getTimelineById(id);

        if (tl == null) {
            throw new InvalidIdException(id, InvalidIdType.TIMELINE);
        }

        return tl;
    }

    public Response setLikes(TimelineRequest.SetLikes request) {
        long tlId = request.getTimelineId();
        int likes = request.getLikes();

        Timeline timeline = getTimelineById(tlId);

        timeline.setLikes(likes);

        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Likes!")
            .addObject("timeline", timeline)
            .build();
    }

    public Response setDescription(TimelineRequest.RenameTimeline request) {
        long tlId = request.getTimelineId();
        String str = request.getName();

        Timeline timeline = getTimelineById(tlId);

        timeline.setDescription(str);

        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Description!")
            .addObject("timeline", timeline)
            .build();
    }

    public Response createEvent(TimelineRequest.CreateEvent request) {
        long timelineId = request.getTimelineId();
        String name = request.getName();
        String description = request.getDescription();
        LocalDateTime date = request.getDate();
        Importance importance = request.getImportance();

        Timeline timeline = getTimelineById(timelineId);

        Event event = new Event(timeline, date, name, description, importance);

        timeline.getEvents().add(event);

        event = eventRepositoryService.save(event);

        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Created Event!")
            .addObject("event", event)
            .build();
    }


    public Response deleteEvent(long id) {
        Event event = getEventById(id);

        event.getTimeline().getEvents().remove(event);

        timelineRepositoryService.save(event.getTimeline());

        eventRepositoryService.delete(event);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Deleted Event!")
            .build();
    }

    public Event getEventById(long id) {
        Event event = eventRepositoryService.getById(id);

        if (event == null) {
            throw new InvalidIdException(id, InvalidIdType.EVENT);
        }

        return event;
    }

    public Response setEventDate(TimelineRequest.SetEventDate request) {
        long eventId = request.getEventId();
        LocalDateTime date = request.getDate();

        Event event = getEventById(eventId);

        event.setLocalDate(date);

        event = eventRepositoryService.save(event);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Event Date!")
            .addObject("event", event)
            .build();
    }

    public Response setEventName(TimelineRequest.SetEventName request) {
        long eventId = request.getEventId();
        String name = request.getName();

        Event event = getEventById(eventId);

        event.setName(name);

        event = eventRepositoryService.save(event);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Event Name!")
            .addObject("event", event)
            .build();
    }

    public Response setEventDescription(TimelineRequest.SetEventName request) {
        long eventId = request.getEventId();
        String name = request.getName();

        Event event = getEventById(eventId);

        event.setDescription(name);

        event = eventRepositoryService.save(event);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Event Description!")
            .addObject("event", event)
            .build();
    }

    public Response setEventImportance(TimelineRequest.SetEventImportance request) {
        long eventId = request.getEventId();
        Importance importance = request.getImportance();

        Event event = getEventById(eventId);

        event.setImportance(importance);

        event = eventRepositoryService.save(event);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Event Importance!")
            .addObject("event", event)
            .build();
    }


    public Response createMarker(TimelineRequest.CreateMarker request) {
        long timelineId = request.getTimelineId();
        String name = request.getName();
        LocalDateTime date = request.getDate();

        Timeline timeline = getTimelineById(timelineId);

        Marker marker = new Marker(timeline, name, date);

        marker = markerRepositoryService.save(marker);
        timeline.getMarkers().add(marker);

        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Created Marker!")
            .addObject("marker", marker)
            .build();
    }

    public Response deleteMarker(long id) {
        Marker marker = getMarkerById(id);

        marker.getTimeline().getMarkers().remove(marker);

        timelineRepositoryService.save(marker.getTimeline());

        markerRepositoryService.delete(marker);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Deleted Marker!")
            .build();
    }

    private Marker getMarkerById(long id) {
        Marker marker = markerRepositoryService.getById(id);

        if (marker == null) {
            throw new InvalidIdException(id, InvalidIdType.MARKER);
        }

        return marker;
    }

    public Response setMarkerName(TimelineRequest.SetMarkerName request) {
        long markerId = request.getMarkerId();
        String name = request.getName();

        Marker marker = getMarkerById(markerId);

        marker.setName(name);

        marker = markerRepositoryService.save(marker);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Marker Name!")
            .addObject("marker", marker)
            .build();
    }

    public Response setMarkerDate(TimelineRequest.SetMarkerDate request) {
        long markerId = request.getMarkerId();
        LocalDateTime date = request.getLocalDateTime();

        Marker marker = getMarkerById(markerId);

        marker.setDate(date);

        marker = markerRepositoryService.save(marker);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Marker Date!")
            .addObject("marker", marker)
            .build();
    }
}
