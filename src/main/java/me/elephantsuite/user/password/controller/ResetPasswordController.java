package me.elephantsuite.user.password.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "password")
@AllArgsConstructor
public class ResetPasswordController {

	private final ResetPasswordService service;

	@PostMapping("sendEmail")
	public Response sendEmail(@RequestParam("id") long userId) {
		return service.sendEmail(userId);
	}

	@PostMapping("resetPassword")
	public Response resetPassword(@RequestBody ResetPasswordRequest.ResetPassword request) {
		return service.resetPassword(request);
	}
}
