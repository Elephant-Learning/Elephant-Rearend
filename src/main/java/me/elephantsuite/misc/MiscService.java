package me.elephantsuite.misc;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
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
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
				.addObject("request", request)
				.build();
		}

		if (!user.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User not enabled!")
				.addObject("user", user)
				.build();
		}

		if (pfpid < 0 || pfpid > 47) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "PFP ID out of bounds! (Needs to be in between 1 and 47 inclusive!)")
				.addObject("user", user)
				.build();
		}

		user.setPfpId(pfpid);

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Successfully set users PFP ID!")
			.addObject("user", user)
			.build();
	}
}
