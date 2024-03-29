package me.elephantsuite.login;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import me.elephantsuite.registration.EmailValidator;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.stats.controller.ElephantUserStatisticsService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.ElephantUserType;
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

	private final ElephantUserStatisticsService elephantUserStatisticsService;

	public Response login(LoginRequest request, boolean stats) {

		String email = request.getEmail();
		String password = request.getPassword();

		if (!emailValidator.test(email)) {
			return ResponseUtil.getFailureResponse("Email not in correct format!", request);
		}

		if (!elephantUserService.isUserAlreadyRegistered(email)) {
			return ResponseUtil.getFailureResponse("Email was not registered to any user", request);
		}

		ElephantUser user = elephantUserService.getUserByEmail(email);

		if (user == null) {
			// keep default msg cuz not user id
			return ResponseUtil.getFailureResponse("Email was not registered to any user!", request);
		}

		if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return ResponseUtil.getFailureResponse("Invalid password!", request);
		}

		if (stats) {
			elephantUserStatisticsService.modifyStatsOnLogin(user.getId());
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "User Info Authenticated and Retrieved!")
			.addObject("user", user)
			.build();
	}

	public Response loginStudent(LoginRequest request) {
		String email = request.getEmail();
		String password = request.getPassword();

		if (!emailValidator.test(email)) {
			return ResponseUtil.getFailureResponse("Email not in correct format!", request);
		}

		if (!elephantUserService.isUserAlreadyRegistered(email)) {
			return ResponseUtil.getFailureResponse("Email was not registered to any user", request);
		}

		ElephantUser user = elephantUserService.getUserByEmail(email);

		if (user == null) {
			// keep default msg cuz not user id
			return ResponseUtil.getFailureResponse("Email was not registered to any user!", request);
		}

		if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return ResponseUtil.getFailureResponse("Invalid password!", request);
		}

		if (!user.getType().equals(ElephantUserType.STUDENT)) {
			return ResponseUtil.getFailureResponse("Tried to login to student login but account did not have STUDENT type!", request);
		}

		elephantUserStatisticsService.modifyStatsOnLogin(user.getId());

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Student Info Authenticated and Retrieved!")
			.addObject("user", user)
			.build();
	}

	public Response loginTeacher(LoginRequest request) {
		String email = request.getEmail();
		String password = request.getPassword();

		if (!emailValidator.test(email)) {
			return ResponseUtil.getFailureResponse("Email not in correct format!", request);
		}

		if (!elephantUserService.isUserAlreadyRegistered(email)) {
			return ResponseUtil.getFailureResponse("Email was not registered to any user", request);
		}

		ElephantUser user = elephantUserService.getUserByEmail(email);

		if (user == null) {
			// keep default msg cuz not user id
			return ResponseUtil.getFailureResponse("Email was not registered to any user!", request);
		}

		if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return ResponseUtil.getFailureResponse("Invalid password!", request);
		}

		if (!user.getType().equals(ElephantUserType.TEACHER)) {
			return ResponseUtil.getFailureResponse("Tried to login to teacher login but account did not have TEACHER type!", request);
		}

		elephantUserStatisticsService.modifyStatsOnLogin(user.getId());

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Teacher Info Authenticated and Retrieved!")
			.addObject("user", user)
			.build();
	}

	public Response getUserById(long id) {
		ElephantUser user = ResponseUtil.checkUserValid(id, elephantUserService);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved User Info!")
			.addObject("user", user)
			.build();
	}

	public Response getUserByEmail(String email) {
		if (!elephantUserService.isUserAlreadyRegistered(email)) {
			return ResponseUtil.getFailureResponse("Email was not registered!", email);
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved User by Email!")
			.addObject("user", elephantUserService.getUserByEmail(email))
			.build();
	}

	public Response getUserByName(String name, long userId) {
		List<ElephantUser> filteredUsers = elephantUserService
			.getAllUsers()
			.stream()
			.filter(elephantUser -> StringUtils.containsIgnoreCase(elephantUser.getFullName(), name))
			.collect(Collectors.toList());

		if (userId != -1) {
			ElephantUser user = ResponseUtil.checkUserValid(userId, elephantUserService);

			filteredUsers = filteredUsers
				.stream()
				.filter(user1 -> !user1.equals(user))
				.sorted((o1, o2) -> compareCountries(o1, o2, user))
				.collect(Collectors.toList());
		}

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

	public Response getUserByNameNoId(String name) {
		List<ElephantUser> filteredUsers = elephantUserService
				.getAllUsers()
				.stream()
				.filter(elephantUser -> StringUtils.containsIgnoreCase(elephantUser.getFullName(), name))
				.toList();

		return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Retrieved Users By Name!")
				.addObject("users", filteredUsers)
				.build();
	}


}
