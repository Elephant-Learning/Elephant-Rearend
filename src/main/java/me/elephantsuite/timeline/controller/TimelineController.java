package me.elephantsuite.timeline.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("timeline")
@AllArgsConstructor
public class TimelineController {

    private final TimelineService service;

    @PostMapping("create")
    public Response createTimeline(@RequestBody TimelineRequest.CreateTimeline request) {
        return service.createTimeline(request);
    }

    @DeleteMapping("delete")
    public Response deleteTimeline(@RequestParam("id") long id) {
        return service.deleteTimeline(id);
    }

    @PostMapping("renameTimeline")
    public Response renameTimeline(@RequestBody TimelineRequest.RenameTimeline request) {
        return service.renameTimeline(request);
    }

    @PostMapping("setVisibility")
    public Response setVisibility(@RequestBody TimelineRequest.SetVisibility request) {
        return service.setVisibility(request);
    }

    @PostMapping("setLikes")
    public Response setLikes(@RequestBody TimelineRequest.SetLikes request) {
        return service.setLikes(request);
    }

    @PostMapping("setDescription")
    public Response setDescription(@RequestBody TimelineRequest.RenameTimeline request) {
        return service.setDescription(request);
    }

    @PostMapping("createEvent")
    public Response createEvent(@RequestBody TimelineRequest.CreateEvent request) {
        return service.createEvent(request);
    }

    @DeleteMapping("deleteEvent")
    public Response deleteEvent(@RequestParam("id") long id) {
        return service.deleteEvent(id);
    }

    @PostMapping("setEventDate")
    public Response setEventDate(@RequestBody TimelineRequest.SetEventDate request) {
        return service.setEventDate(request);
    }

    @PostMapping("setEventName")
    public Response setEventName(@RequestBody TimelineRequest.SetEventName request) {
        return service.setEventName(request);
    }

    @PostMapping("setEventDescription")
    public Response setEventDescription(@RequestBody TimelineRequest.SetEventName request) {
        return service.setEventDescription(request);
    }

    @PostMapping("setEventImportance")
    public Response setEventImportance(@RequestBody TimelineRequest.SetEventImportance request) {
        return service.setEventImportance(request);
    }

    @PostMapping("createMarker")
    public Response createMarker(@RequestBody TimelineRequest.CreateMarker request) {
        return service.createMarker(request);
    }

    @DeleteMapping("deleteMarker")
    public Response deleteMarker(@RequestParam("id") long id) {
        return service.deleteMarker(id);
    }

    @PostMapping("setMarkerName")
    public Response setMarkerName(@RequestBody TimelineRequest.SetMarkerName request) {
        return service.setMarkerName(request);
    }

    @PostMapping("setMarkerDate")
    public Response setMarkerDate(@RequestBody TimelineRequest.SetMarkerDate request) {
        return service.setMarkerDate(request);
    }
}
