package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "misc")
@AllArgsConstructor
public class ElephantMiscController {

	private final MiscService miscService;

	public Response setPfpId(MiscRequest.SetPfpId request) {
		return miscService.setPfpId(request);
	}
}
