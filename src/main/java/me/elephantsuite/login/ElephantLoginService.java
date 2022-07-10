package me.elephantsuite.login;

import lombok.AllArgsConstructor;
import me.elephantsuite.registration.EmailValidator;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantLoginService {

	private final ElephantUserService elephantUserService;
	private final EmailValidator emailValidator;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public String login(LoginRequest request) {

		String email = request.getEmail();
		String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());

		if (!emailValidator.test(email)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Email not in correct format!")
				.addObject("loginInfo", request)
				.build();
		}

		ElephantUser user = elephantUserService.getUserByEmail(email).orElse(null);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Email was not registered to any user!")
				.addObject("loginInfo", request)
				.build();
		}

		if (!user.getPassword().equals(encryptedPassword)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid password!")
				.addObject("loginInfo", request)
				.addObject("user", user)
				.build();
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "User Info Retrieved!")
			.addObject("user", user)
			.build();
	}
}
