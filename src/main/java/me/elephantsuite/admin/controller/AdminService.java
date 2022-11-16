package me.elephantsuite.admin.controller;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.InvalidPasswordException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.config.UserConfig;
import me.elephantsuite.user.config.UserConfigRepositoryService;
import me.elephantsuite.user.config.controller.UserConfigService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AdminService {

	private final ElephantUserService userService;

	private final BCryptPasswordEncoder encoder;

	private final UserConfigRepositoryService userConfigService;
	public Response refreshUserConfigs(AdminRequest.AuthRequest request) {
		long id = request.getId();
		String password = request.getPassword();

		ElephantUser user = userService.getUserById(id);

		if (user == null) {
			throw new InvalidIdException(request, InvalidIdType.USER);
		}

		if (!passwordMatches(password, user)) {
			throw new InvalidPasswordException(password);
		}

		List<ElephantUser> nullConfigs = userService
			.getAllUsers()
			.stream()
			.filter(user1 -> user1.getConfig() == null).toList();

		nullConfigs.forEach(user1 -> {
			UserConfig config = new UserConfig(user1);
			user1.setConfig(config);
			userConfigService.save(config);
			userService.saveUser(user1);
		});

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Refreshed User Configs!")
			.build();

	}

	private boolean passwordMatches(String password, ElephantUser user) {
		return encoder.matches(password, user.getPassword());
	}
}
