package me.elephantsuite.registration;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.base.Throwables;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.email.EmailSender;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.registration.token.ConfirmationTokenService;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RegistrationService {

	private final ElephantUserService elephantUserService;

	private final EmailValidator emailValidator;

	private final ConfirmationTokenService confirmationTokenService;

	private final EmailSender emailSender;

	// when given a request process it
	public Response register(RegistrationRequest request) {
		if (emailValidator.test(request.getEmail())) {
			ElephantUser elephantUser = new ElephantUser(
				request.getFirstName(),
				request.getLastName(),
				request.getEmail(),
				request.getPassword(),
				request.getType(),
				request.getPfpId()
			);

			if (elephantUserService.isUserAlreadyRegistered(elephantUser)) {
				elephantUser = elephantUserService.getUserById(elephantUserService.getUserId(elephantUser));
				ConfirmationToken token = elephantUser.getToken(); // can ignore nullable warning
				if (token != null) {
					// resend email if after 15 mins
					LocalDateTime expiresAt = token.getExpiresAt();
					if (LocalDateTime.now().isAfter(expiresAt)) {

						if (!ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("isDevelopment", Boolean::parseBoolean)) {
							// reset expiration to be due in another 15 minutes
							confirmationTokenService.addExpiredLimit(token, 15);

							String link = ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("elephantDomain", Function.identity()) + "/user/registration/confirm?token=" + token.getToken();

							try {
								emailSender.send(elephantUser.getEmail(), "<h1> ey " + elephantUser.getFirstName() + " click dis <a href=\"" + link + "\">link</a> fo free fall guyz coins </h1>", true);
							} catch (IllegalStateException e) {
								return ResponseBuilder
									.create()
									.addResponse(ResponseStatus.FAILURE, "Exception while emailing link to user!")
									.addException(e)
									.addObject("user", elephantUser)
									.build();
							}
						}

						return ResponseBuilder
							.create()
							.addResponse(ResponseStatus.DEFER, "Token expired, resent email and token renewed")
							.addObject("user", elephantUser)
							.build();
					}

					return ResponseBuilder
						.create()
						.addResponse(ResponseStatus.DEFER, "Check Email to activate token")
						// client should build link off this token
						.addObject("user", elephantUser)
						.build();
				}

				//if null then token already used and email is confirmed already registered
				return ResponseBuilder
					.create()
					.addResponse(ResponseStatus.FAILURE, "User already registered and validated")
					.addObject("user", elephantUser) // can ignore
					.build();
			}

			ConfirmationToken token = elephantUserService.signUpUser(elephantUser);

			String link = ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("elephantDomain", Function.identity()) + "/user/registration/confirm?token=" + token.getToken();

			if (!ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("isDevelopment", Boolean::parseBoolean)) {
				try {
					emailSender.send(elephantUser.getEmail(), "<h1> ey " + request.getFirstName() + " click dis <a href=\"" + link + "\">link</a> fo free fall guyz coins </h1>", true);
				} catch (IllegalStateException e) {
					return ResponseBuilder
						.create()
						.addResponse(ResponseStatus.FAILURE, "Exception while emailing link to user!")
						.addException(e)
						.addObject("user", elephantUser)
						.build();
				}
			}
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "token confirmed, email sent")
				.addObject("user", elephantUser)
				.addObject("link", link)
				.build();
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "a valid email was not sent!")
			.addObject("request", request)
			.build();
	}

	@Transactional
	public Response confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElse(null);

		if (confirmationToken == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Insert a valid token!")
				.addObject("token", token)
				.build();
		}

		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Token Expired")
				.addObject("token", confirmationToken)
				.build();
		}

		confirmationToken.getElephantUser().setToken(null);

		confirmationToken.getElephantUser().setEnabled(true);

		elephantUserService.saveUser(confirmationToken.getElephantUser());

		confirmationTokenService.deleteToken(confirmationToken);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Account Enabled")
			.addObject("user", confirmationToken.getElephantUser())
			.build();
	}

	public Response deleteUser(long id) {
		ElephantUser user = elephantUserService.getUserById(id);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
				.addObject("userId", id)
				.build();
		}

		elephantUserService.deleteUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted User!")
			.addObject("user", user)
			.build();
	}
}
