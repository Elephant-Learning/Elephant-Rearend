package me.elephantsuite.registration;

import java.time.LocalDateTime;

import me.elephantsuite.email.EmailSender;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.registration.token.ConfirmationTokenService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {


	private final ElephantUserService elephantUserService;
	private final EmailValidator emailValidator;
	private final ConfirmationTokenService confirmationTokenService;
	private final EmailSender emailSender;

	// when given a request process it
	public String register(RegistrationRequest request) {
		if (emailValidator.test(request.getEmail())) {
			ElephantUser elephantUser = new ElephantUser(
				request.getFirstName(),
				request.getLastName(),
				request.getEmail(),
				request.getPassword(),
				request.getType()
			);

			String token = elephantUserService.signUpUser(elephantUser);

			String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;

			emailSender.send(elephantUser.getEmail(), "<h1> ey " + request.getFirstName() + " click dis <a href=\"" + link + "\">link</a> fo free fall guyz coins </h1>" );

			//TODO return status to be used on client
			return token;
		}

		throw new IllegalStateException("that's cringe bro couldn't type an email in :skull: :skull: (Email: \"" + request.getEmail() + "\")");
	}

	@Transactional
	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("Token not found"));

		if (confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("email already confirmed");
		}

		LocalDateTime expiredAt = confirmationToken.getExpiresAt();

		if (expiredAt.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("token expired");
		}

		confirmationTokenService.setConfirmedAt(token);

		elephantUserService.enableAppUser(confirmationToken.getElephantUser().getEmail());

		return "confirmed";
	}

}
