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
}
