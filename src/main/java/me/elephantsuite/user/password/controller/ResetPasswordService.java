package me.elephantsuite.user.password.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.email.EmailService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.password.ResetPasswordToken;
import me.elephantsuite.user.password.ResetPasswordTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResetPasswordService {

	private final ElephantUserService userService;

	private final ResetPasswordTokenService service;

	private final EmailService emailService;

	private final BCryptPasswordEncoder encoder;

	public Response sendEmail(long userId) {
		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			throw new InvalidIdException(userId, InvalidIdType.USER);
		}

		//TODO add replace values

		ResetPasswordToken token = new ResetPasswordToken(UUID.randomUUID().toString(), LocalDateTime.now().plusMinutes(ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("tokenExpiredLimitMinutes", Long::parseLong)), user);

		emailService.send(user.getEmail(), ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("forgotPasswordEmailHtmlFile").replace("[UUID]", token.getToken()), "Reset Your Password" ,true);


		user.setResetPasswordToken(token);

		service.save(token);

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Sent Reset Password Email to you!")
			.addObject("token", token)
			.build();
	}

	public Response resetPassword(ResetPasswordRequest.ResetPassword request) {
		String token = request.getToken();
		String newPassword = request.getNewPassword();

		ResetPasswordToken resetToken = service.getByToken(token);

		if (resetToken == null) {
			throw new InvalidIdException(request, InvalidIdType.RESET_TOKEN);
		}

		if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("tokenExpiredLimitMinutes", Long::parseLong)));

			resetToken = service.save(resetToken);

			emailService.send(resetToken.getElephantUser().getEmail(), ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("forgotPasswordEmailHtmlFile").replace("[UUID]", resetToken.getToken()), "Reset your password" ,true);

			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.DEFER, "Token Expired, Please check your email again")
				.addObject("resetToken", resetToken)
				.build();
		}

		String encodedPassword = encoder.encode(newPassword);

		ElephantUser user = resetToken.getElephantUser();

		if (encoder.matches(newPassword, user.getPassword())) {
			user.setResetPasswordToken(null);
			service.delete(resetToken);

			userService.saveUser(user);

			return ResponseUtil.getFailureResponse("Password was the same as your old one!", request);

		}

		user.setPassword(encodedPassword);
		user.setResetPasswordToken(null);

		service.delete(resetToken);

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Reset Password!")
			.addObject("user", user)
			.build();
	}
}
