package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.config.PropertiesHandler;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
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

	public Response setPfpId(MiscRequest.SetPfpId request) {
		long userId = request.getUserId();
		int pfpid = request.getPfpId();

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			throw new InvalidIdException(request, InvalidIdType.USER);
		}

		if (!user.isEnabled()) {
			throw new UserNotEnabledException(user);
		}

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
		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			throw new InvalidIdException(userId, InvalidIdType.USER);
		}

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

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			throw new InvalidIdException(request, InvalidIdType.USER);
		}

		user.setCountryCode(countryCode);

		userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Set User's Country Code!")
			.addObject("user", user)
			.build();
	}
}
