package me.elephantsuite.login;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
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
		return elephantLoginService.login(request, true);
	}
	// same as login but does not run login stuff
	@PostMapping("verifyUser")
	public Response verifyUser(@RequestBody LoginRequest request) {
		return elephantLoginService.login(request, false);
	}

	@GetMapping(path = "user")
	public Response getUserById(@RequestParam("id") long id) {
		return elephantLoginService.getUserById(id);
	}

	@GetMapping(path = "userByEmail")
	public Response getUserByEmail(@RequestParam("email") String email) {
		return elephantLoginService.getUserByEmail(email);
	}

	@GetMapping(path = "userByName")
	public Response getUserByName(@RequestParam("userId") long userId, @RequestParam("name") String name) {
		return elephantLoginService.getUserByName(name, userId);
	}

	@GetMapping(path = "userByNameNoId")
	public Response getUserByNameNoId(@RequestParam("name") String name) {
		return elephantLoginService.getUserByNameNoId(name);
	}
}
