package me.elephantsuite.timeline.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.stats.medal.MedalService;
import me.elephantsuite.stats.medal.MedalType;
import me.elephantsuite.timeline.Timeline;
import me.elephantsuite.timeline.TimelineRepository;
import me.elephantsuite.timeline.TimelineRepositoryService;
import me.elephantsuite.timeline.TimelineVisibility;
import me.elephantsuite.timeline.event.Event;
import me.elephantsuite.timeline.event.EventRepositoryService;
import me.elephantsuite.timeline.event.Importance;
import me.elephantsuite.timeline.marker.Marker;
import me.elephantsuite.timeline.marker.MarkerRepositoryService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.apache.commons.lang3.StringUtils;
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

    private final TimelineRepository timelineRepository;

    private final MedalService medalService;

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

        medalService.updateEntityMedals(user.getTimelines(), user.getElephantUserStatistics(), MedalType.TIME_MASTER, new int[]{2, 8, 16, 64});

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

        timeline.getMarkers().forEach(markerRepositoryService::delete);
        timeline.getEvents().forEach(eventRepositoryService::delete);

        timeline.getMarkers().clear();
        timeline.getEvents().clear();

        timelineRepositoryService.save(timeline);

        timelineRepository.deleteLikedTimelineIds(id);
        timelineRepository.deleteSharedTimelineIds(id);
        timelineRepository.deleteFolderTimelineIds(id);
        timelineRepository.deleteRecentlyViewedTimelineIds(id);

      //  timeline.setEvents(new ArrayList<>());
      //  timeline.setMarkers(new ArrayList<>());

        timelineRepository.deleteById(id);



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
        timeline.updateLastUpdated();
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
        timeline.updateLastUpdated();
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
        timeline.updateLastUpdated();
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
        timeline.updateLastUpdated();
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
        String date = request.getDate();
        String endDate = request.getEndDate();
        Importance importance = request.getImportance();
        String image = request.getImage();

        Timeline timeline = getTimelineById(timelineId);

        Event event = new Event(timeline, date, name, description, importance, endDate, image);

        timeline.getEvents().add(event);
        timeline.updateLastUpdated();
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
        String date = request.getDate();

        Event event = getEventById(eventId);

        event.setStartDate(date);

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
        String date = request.getDate();

        Timeline timeline = getTimelineById(timelineId);
        timeline.updateLastUpdated();
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
        String date = request.getDate();

        Marker marker = getMarkerById(markerId);

        marker.setDate(date);

        marker = markerRepositoryService.save(marker);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Marker Date!")
            .addObject("marker", marker)
            .build();
    }

    public Response setEventEndDate(TimelineRequest.SetEventDate request) {
        long eventId = request.getEventId();
        String date = request.getDate();

        Event event = getEventById(eventId);

        event.setEndDate(date);

        event = eventRepositoryService.save(event);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Set Event Date!")
            .addObject("event", event)
            .build();
    }

    public Response getNumberOfTimelines() {
        List<Timeline> timelines = this.timelineRepositoryService.getTimelines();

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Retrieved Timeline Number!")
            .addObject("timelines", timelines.size())
            .build();
    }

    public Response shareTimeline(TimelineRequest.ShareTimeline request) {
        long userId = request.getUserId();
        long timelineId = request.getTimelineId();

        Timeline timeline = getTimelineById(timelineId);
        timeline.updateLastUpdated();
        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        if (!timeline.getTimelineVisibility().equals(TimelineVisibility.SHARED)) {
            return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.FAILURE, "Timeline Visibility must be set to SHARED to share Timelines with other users!")
                .addObject("timeline", timeline)
                .build();
        }

        timeline.getSharedUsers().add(userId);
        user.getSharedTimelineIds().add(timelineId);

        userService.saveUser(user);
        timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Shared Timeline!")
            .addObject("timeline", timeline)
            .build();
    }

    public Response searchTimelines(long userId, String query) {
        List<Timeline> timelines = timelineRepositoryService.getTimelines()
            .stream()
            .filter(timeline -> {
                if (timeline.getTimelineVisibility().equals(TimelineVisibility.SHARED)) {
                    return !timeline.getSharedUsers().contains(userId);
                }

                if (timeline.getTimelineVisibility().equals(TimelineVisibility.PRIVATE)) {
                    return !timeline.getUser().getId().equals(userId);
                }

                return true;
            })
            .filter(timeline -> StringUtils.containsIgnoreCase(timeline.getName(), query))
            .toList();

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Retrieved Timelines!")
            .addObject("timelines", timelines)
            .build();
    }

    public Response getTimelineById(long userId, long timelineId) {
        Timeline timeline = getTimelineById(timelineId);
        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        if (timeline.getTimelineVisibility() == TimelineVisibility.PRIVATE && !timeline.getUser().equals(user)) {
           return ResponseBuilder
               .create()
               .addResponse(ResponseStatus.FAILURE, "Timeline was private and user was not the owner!")
               .build();
        } else if (timeline.getTimelineVisibility() == TimelineVisibility.SHARED && !timeline.getSharedUsers().contains(userId)) {
            return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.FAILURE, "Timeline was shared but user did not have the timeline shared with them!")
                .build();
        }

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Retrieved Timeline!")
            .addObject("timeline", timeline)
            .build();
    }

    public Response likeTimeline(TimelineRequest.LikeTimeline request) {
        long userId = request.getUserId();
        long timelineId = request.getTimelineId();

        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);
        Timeline timeline = getTimelineById(timelineId);

        timeline.incrementLikes();
        user.getLikedTimelineIds().add(timelineId);
        timeline.updateLastUpdated();
        user = userService.saveUser(user);
        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Liked Timeline!")
            .addObject("timeline", timeline)
            .build();
    }

    public Response unlikeTimeline(TimelineRequest.LikeTimeline request) {
        long userId = request.getUserId();
        long timelineId = request.getTimelineId();

        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);
        Timeline timeline = getTimelineById(timelineId);

        timeline.decrementLikes();
        user.getLikedTimelineIds().remove(timelineId);
        timeline.updateLastUpdated();
        user = userService.saveUser(user);
        timeline = timelineRepositoryService.save(timeline);

        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.SUCCESS, "Liked Timeline!")
            .addObject("timeline", timeline)
            .build();
    }
}
