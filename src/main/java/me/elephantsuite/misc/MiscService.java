package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.config.PropertiesHandler;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.email.EmailService;
import me.elephantsuite.registration.EmailValidator;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.UserNotEnabledException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MiscService {

	private final ElephantUserService userService;

	private final EmailValidator emailValidator;

	private final EmailService emailService;

	private final CardService cardService;

	public Response setPfpId(MiscRequest.SetPfpId request) {
		long userId = request.getUserId();
		int pfpid = request.getPfpId();

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		PropertiesHandler handler = ElephantBackendApplication.ELEPHANT_CONFIG;

		if (pfpid < 0 || pfpid > handler.getConfigOption("pfpIdMax", Integer::parseInt)) {
			return ResponseUtil.getFailureResponse("PFP ID out of bounds! (Needs to be in between 1 and 47 inclusive!)", request);
		}

		user.setPfpId(pfpid);

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Successfully set users PFP ID!")
			.addObject("user", user)
			.build();
	}

    public Response setNewUserFalse(long userId) {
		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		user.setNewUser(false);

		userService.saveUser(user);

		return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Set New User False!")
				.addObject("user", user)
				.build();
    }

	public Response setCountryCode(MiscRequest.SetCountryCode request) {
		long userId = request.getUserId();
		int countryCode = request.getCountryCode();

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		user.setCountryCode(countryCode);

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Set User's Country Code!")
			.addObject("user", user)
			.build();
	}

	public Response inviteUser(MiscRequest.InviteUser request) {
		long userId = request.getUserId();
		String email = request.getEmailToInvite();

		ResponseUtil.checkUserValid(userId, userService);

		if (!emailValidator.test(email)) {
			return ResponseUtil.getFailureResponse("Invalid E-Mail!", request);
		}

		ElephantUser user1 = userService.getUserByEmail(email);

		if (user1 != null) {
			return ResponseUtil.getFailureResponse("Email already registered with elephant!", request);
		}

		emailService.send(email, ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("inviteEmailHtmlFile"), "You have been invited to Elephant!", true);

		return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Invited " + email + " to Elephant!")
				.addObject("request", request)
				.build();
	}

	public Response getTotalUsersAndCards() {
		int users = userService.getAllUsers().size();
		int cards = cardService.getAllCards().size();

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved number of users and cards!")
			.addObject("users", users)
			.addObject("cards", cards)
			.build();
	}
}
