package me.elephantsuite.user.config.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "userConfig")
@AllArgsConstructor
public class UserConfigController {

	private final UserConfigService userConfigService;

	@PostMapping("changeBooleanValue")
	public Response changeBooleanValue(@RequestBody UserConfigRequest.ChangeBooleanValue request) {
		return userConfigService.changeBooleanValue(request);
	}
}
