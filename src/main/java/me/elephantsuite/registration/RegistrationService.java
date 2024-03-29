package me.elephantsuite.registration;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.email.EmailSender;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.registration.token.ConfirmationTokenService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RegistrationService {

	private static final Pattern HTML_PATTERN = Pattern.compile("(.*</\\w*>)|(.*</\\w*)|(.*<\\w*>)");
	private final ElephantUserService elephantUserService;

	private final EmailValidator emailValidator;

	private final ConfirmationTokenService confirmationTokenService;

	private final EmailSender emailSender;

	private final BCryptPasswordEncoder encoder;

	// when given a request process it
	public Response register(RegistrationRequest.CreateAccount request) {

		if (request.getPassword().isBlank() || request.getPassword().length() < 4) {
			return ResponseUtil.getFailureResponse("Password cannot be blank or be less than 4 characters!", request);
		}

		String fullName = request.getFirstName() + request.getLastName();

		if (isInvalidName(request.getFirstName()) || isInvalidName(request.getLastName()) || isInvalidName(fullName)) {
			throw new InvalidTagInputException(fullName);
		}

		if (isInvalidName(request.getEmail())) {
			throw new InvalidTagInputException(request.getEmail());
		}

		if (emailValidator.test(request.getEmail())) {
			ElephantUser elephantUser = new ElephantUser(
				request.getFirstName(),
				request.getLastName(),
				request.getEmail(),
				request.getPassword(),
				request.getType(),
				request.getCountryCode(),
				request.getPfpId()
			);

			if (elephantUserService.isUserAlreadyRegistered(request.getEmail())) {
				elephantUser = elephantUserService.getUserById(elephantUserService.getUserId(request.getEmail()));
				ConfirmationToken token = elephantUser.getConfirmationToken(); // can ignore nullable warning
				if (token != null) {
					// resend email if after 15 mins
					LocalDateTime expiresAt = token.getExpiresAt();
					if (LocalDateTime.now().isAfter(expiresAt)) {

						// reset expiration to be due in another 15 minutes
						confirmationTokenService.addExpiredLimit(token, ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("tokenExpiredLimitMinutes", Integer::parseInt));

						try {
							emailSender.send(elephantUser.getEmail(), ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("confirmationEmailHtmlFile").replace("[TOKEN]", token.getToken()), "Confirm your email", true);
						} catch (IllegalStateException e) {
							return ResponseBuilder
								.create()
								.addResponse(ResponseStatus.FAILURE, "Exception while emailing link to user!")
								.addException(e)
								.addObject("user", elephantUser)
								.build();
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
				return ResponseUtil.getFailureResponse("User already registered and validated", request);
			}

			ConfirmationToken token = elephantUserService.signUpUser(elephantUser);

			String link = ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("elephantDomain") + "/registration/confirm?token=" + token.getToken();

			try {
				String html = ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("confirmationEmailHtmlFile").replace("[TOKEN]", token.getToken());
				emailSender.send(elephantUser.getEmail(), html, "Confirm your email", true);
			} catch (IllegalStateException e) {
				return ResponseBuilder
					.create()
					.addResponse(ResponseStatus.FAILURE, "Exception while emailing link to user!")
					.addException(e)
					.addObject("user", elephantUser)
					.build();
			}
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "User Created, Confirmation Token Email Sent!")
				.addObject("user", elephantUser)
				.addObject("link", link)
				.build();
		}

		return ResponseUtil.getFailureResponse("A valid email was not sent!", request);
	}

	@Transactional
	public Response confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElse(null);

		if (confirmationToken == null) {
			throw new InvalidIdException(token, InvalidIdType.CONFIRMATION_TOKEN);
		}

		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {
			return ResponseUtil.getFailureResponse("Token Expired", confirmationToken);
		}

		confirmationToken.getElephantUser().setConfirmationToken(null);

		confirmationToken.getElephantUser().setEnabled(true);

		elephantUserService.saveUser(confirmationToken.getElephantUser());

		confirmationTokenService.deleteToken(confirmationToken);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Account Enabled")
			.addObject("user", confirmationToken.getElephantUser())
			.build();
	}

	public Response deleteUser(RegistrationRequest.DeleteAccount request) {
		long id = request.getId();
		String password = request.getPassword();

		ElephantUser user = ResponseUtil.checkUserValid(id, elephantUserService);

		if (!encoder.matches(password, user.getPassword())) {
			return ResponseUtil.getFailureResponse("Invalid Password!", request);
		}

		elephantUserService.deleteUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted User!")
			.build();
	}

	public static boolean isInvalidName(String str) {
		return HTML_PATTERN.matcher(str).matches();
	}

	public Response setUserDetails(RegistrationRequest.SetAccountDetails request) {
		ElephantUser user = ResponseUtil.checkUserValid(request.getUserId(), elephantUserService);

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setPhoneNumber(request.getPhoneNumber());

		user = elephantUserService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Set User Details!")
			.addObject("user", user)
			.build();
	}
}
