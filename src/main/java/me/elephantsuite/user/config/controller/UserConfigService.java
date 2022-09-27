package me.elephantsuite.user.config.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.config.UserConfig;
import me.elephantsuite.user.config.UserConfigRepositoryService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserConfigService {

	private final UserConfigRepositoryService service;

	private final ElephantUserService userService;

	public Response changeBooleanValue(UserConfigRequest.ChangeBooleanValue request) {
		long userId = request.getUserId();
		String term = request.getTerm();
		boolean value = request.isValue();

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			return ResponseUtil.getInvalidUserResponse(userId);
		}

		Method configMethod = Arrays.stream(UserConfig.class
			.getMethods())
			.filter(method -> method.getName().toLowerCase().startsWith("set" + term.toLowerCase()))
			.findFirst()
			.orElse(null);

		if (configMethod == null) {
			return ResponseUtil.getFailureResponse("Config Term does not exist or is not boolean type!", UserConfig.class.getFields());
		}

		try {
			configMethod.invoke(user.getConfig(), value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return ResponseUtil.getFailureResponse(e.getMessage(), request);
		}

		service.save(user.getConfig());

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Set boolean term \"" + term + "\" to " + value)
			.addObject("user", user)
			.build();
	}
}
