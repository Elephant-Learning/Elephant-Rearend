package me.elephantsuite.login;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/login")
@AllArgsConstructor
public record ElephantUserLoginController(ElephantLoginService elephantLoginService) {

	@PostMapping
	public String login(@RequestBody LoginRequest request) {
		return elephantLoginService.login(request);
	}

	@GetMapping(path = "user")
	public String getUserById(@RequestParam("id") long id) {
		return elephantLoginService.getUserById(id);
	}
}
