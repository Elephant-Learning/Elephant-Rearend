package me.elephantsuite.login;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import me.elephantsuite.registration.EmailValidator;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ElephantLoginService {
	private final ElephantUserService elephantUserService;
	private final EmailValidator emailValidator;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public Response login(LoginRequest request) {

		String email = request.getEmail();
		String password = request.getPassword();

		if (!emailValidator.test(email)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Email not in correct format!")
				.addObject("loginInfo", request)
				.build();
		}

		if (!elephantUserService.isUserAlreadyRegistered(email)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Email was not registered to any user")
				.addObject("loginInfo", request)
				.build();
		}

		ElephantUser user = elephantUserService.getUserByEmail(email);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Email was not registered to any user!")
				.addObject("loginInfo", request)
				.build();
		}

		if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid password!")
				.addObject("loginInfo", request)
				.addObject("user", user)
				.build();
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "User Info Authenticated and Retrieved!")
			.addObject("user", user)
			.build();
	}

	public Response getUserById(long id) {
		ElephantUser user = elephantUserService.getUserById(id);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "ID does not match any user!")
				.addObject("id", id)
				.build();
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved User Info!")
			.addObject("user", user)
			.build();
	}

	public Response getUserByEmail(String email) {
		if (!elephantUserService.isUserAlreadyRegistered(email)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Email was not registered!")
				.addObject("email", email)
				.build();
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved User by Email!")
			.addObject("user", elephantUserService.getUserByEmail(email))
			.build();
	}

	public Response getUserByName(String name, long userId) {

		ElephantUser user = elephantUserService.getUserById(userId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
				.addObject("name", name)
				.addObject("userId", userId)
				.build();
		}

		List<ElephantUser> filteredUsers = elephantUserService
				.getAllUsers()
				.stream()
				.filter(elephantUser -> StringUtils.containsIgnoreCase(elephantUser.getFullName(), name))
				.filter(user1 -> !user1.equals(user))
				.sorted((o1, o2) -> compareCountries(o1, o2, user))
				.collect(Collectors.toList());

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved Users By Name!")
			.addObject("users", filteredUsers)
			.build();
	}

	private static int compareCountries(ElephantUser o1, ElephantUser o2, ElephantUser user) {
		if (o1.getCountryCode() == user.getCountryCode()) {
			return -1;
		}

		if (o2.getCountryCode() == user.getCountryCode()) {
			return 1;
		}

		return 0;
	}
}
