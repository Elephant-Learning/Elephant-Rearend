package me.elephantsuite.registration;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "registration")
@AllArgsConstructor
public class ElephantUserRegistrationController {

	private final RegistrationService registrationService;


	// called on post rq
	@PostMapping
	public Response register(@RequestBody RegistrationRequest request) {
		return registrationService.register(request);
	}

	@PostMapping(path = "confirm")
	public Response confirm(@RequestParam("token") String token) {
		return registrationService.confirmToken(token);
	}

	@DeleteMapping("delete")
	public Response delete(@RequestParam("id") long id) {
		return registrationService.deleteUser(id);
	}

}
