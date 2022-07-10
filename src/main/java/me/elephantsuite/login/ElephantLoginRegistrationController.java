package me.elephantsuite.login;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "login")
@AllArgsConstructor
public class ElephantLoginRegistrationController {

	private final ElephantLoginService elephantLoginService;

	@PostMapping
	public String login(@RequestBody LoginRequest request) {
		return elephantLoginService.login(request);
	}
}
