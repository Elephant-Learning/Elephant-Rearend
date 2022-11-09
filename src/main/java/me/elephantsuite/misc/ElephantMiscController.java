package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "misc")
@AllArgsConstructor
public class ElephantMiscController {

	private final MiscService miscService;

	@PostMapping(path = "pfpid")
	public Response setPfpId(@RequestBody MiscRequest.SetPfpId request) {
		return miscService.setPfpId(request);
	}

	@PostMapping(path = "newUserFalse")
	public Response setNewUserFalse(@RequestParam("id") long userId) {
		return miscService.setNewUserFalse(userId);
	}

	@PostMapping(path = "inviteUser")
	public Response inviteUser(@RequestBody MiscRequest.InviteUser request) {
		return miscService.inviteUser(request);
	}

	@PostMapping(path = "countryCode")
	public Response changeCountryCode(@RequestBody MiscRequest.SetCountryCode request) {
		return miscService.setCountryCode(request);
	}
}
