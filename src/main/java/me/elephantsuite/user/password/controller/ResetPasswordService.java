package me.elephantsuite.user.password.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.email.EmailSender;
import me.elephantsuite.email.EmailService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import me.elephantsuite.user.password.ResetPasswordToken;
import me.elephantsuite.user.password.ResetPasswordTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
			return ResponseUtil.getInvalidUserResponse(userId);
		}

		//TODO add replace values
		emailService.send(user.getEmail(), ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("forgotPasswordEmailHtmlFile"), true);

		ResetPasswordToken token = new ResetPasswordToken(UUID.randomUUID().toString(), LocalDateTime.now().plusMinutes(ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("tokenExpiredLimitMinutes", Long::parseLong)), user);

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
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Reset Token!")
				.addObject("request", request)
				.build();
		}

		String encodedPassword = encoder.encode(newPassword);

		ElephantUser user = resetToken.getElephantUser();

		user.setPassword(encodedPassword);

		service.delete(resetToken);

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Reset Password!")
			.addObject("user", user)
			.build();
	}
}
