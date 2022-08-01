package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "misc")
@AllArgsConstructor
public class ElephantMiscController {

	private final MiscService miscService;

	@PostMapping(path = "pfpid")
	public Response setPfpId(@RequestBody MiscRequest.SetPfpId request) {
		return miscService.setPfpId(request);
	}
}
