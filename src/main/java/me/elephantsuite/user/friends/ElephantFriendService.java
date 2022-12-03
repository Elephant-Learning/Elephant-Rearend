package me.elephantsuite.user.friends;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.email.EmailService;
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
@AllArgsConstructor
@Transactional
public class ElephantFriendService {

	private final ElephantUserService userService;

	public Response addFriend(FriendRequest request) {

		long userId = request.getUserId();
		long friendId = request.getFriendId();

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		ElephantUser friend = ResponseUtil.checkUserValid(friendId, userService);


		if (user.getFriendIds().contains(friendId) || friend.getFriendIds().contains(userId)) {
			return ResponseUtil.getFailureResponse("User already friended!", request);
		}

		if (userId == friendId) {
			return ResponseUtil.getFailureResponse("Cannot friend yourself!", request);
		}


		user.getFriendIds().add(friendId);

		friend.getFriendIds().add(userId);

		user = this.userService.saveUser(user);

		friend = this.userService.saveUser(friend);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Friend added to User!")
			.addObject("user", user)
			.addObject("friend", friend)
			.build();
	}

	public Response removeFriend(FriendRequest request) {
		long userId = request.getUserId();
		long friendId = request.getFriendId();

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		ElephantUser friend = ResponseUtil.checkUserValid(friendId, userService);

		if (!user.getFriendIds().contains(friendId) || !friend.getFriendIds().contains(userId)) {
			return ResponseUtil.getFailureResponse("Cannot unfriend someone who is not friends already!", request);
		}

		if (userId == friendId) {
			return ResponseUtil.getFailureResponse("Cannot unfriend yourself!", request);
		}


		user.getFriendIds().remove(friendId);

		friend.getFriendIds().remove(userId);

		this.userService.saveUser(user);

		this.userService.saveUser(friend);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Friend removed from User!")
			.addObject("user", user)
			.addObject("friend", friend)
			.build();
	}
}
