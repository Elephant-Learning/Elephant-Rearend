package me.elephantsuite.login;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "login")
@AllArgsConstructor
public class ElephantLoginController {
	private final ElephantLoginService elephantLoginService;

	@PostMapping
	public Response login(@RequestBody LoginRequest request) {
		return elephantLoginService.login(request);
	}

	@GetMapping(path = "user")
	public Response getUserById(@RequestParam("id") long id) {
		return elephantLoginService.getUserById(id);
	}

	@GetMapping(path = "userByEmail")
	public Response getUserByEmail(@RequestParam("email") String email) {
		return elephantLoginService.getUserByEmail(email);
	}
}
