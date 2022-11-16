package me.elephantsuite.admin.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "admin")
@AllArgsConstructor
public class AdminController {

	private final AdminService service;

	// if user configs are null, reinitialize them for past users
	@PostMapping(path = "refreshUserConfigs")
	public Response refreshUserConfigs(@RequestBody AdminRequest.AuthRequest request) {
		return service.refreshUserConfigs(request);
	}
}
