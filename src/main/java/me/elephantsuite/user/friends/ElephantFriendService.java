package me.elephantsuite.user.friends;

import java.util.Objects;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
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

		ElephantUser user = this.userService.getUserById(userId);

		ElephantUser friend = this.userService.getUserById(friendId);

		if (user == null || friend == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User or Friend IDs are invalid!")
				.addObject("userId", userId)
				.addObject("friendId", friendId)
				.build();
		}

		if (!user.isEnabled() || !friend.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User or Friend are not enabled!")
				.addObject("user", user)
				.addObject("friend", friend)
				.build();
		}

		if (user.getFriendIds().contains(friendId) || friend.getFriendIds().contains(userId)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User already friended!")
				.addObject("user", user)
				.addObject("friend", friend)
				.build();
		}

		if (userId == friendId) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Cannot friend yourself!")
				.addObject("user", user)
				.addObject("request", request)
				.build();
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

		ElephantUser user = this.userService.getUserById(userId);

		ElephantUser friend = this.userService.getUserById(friendId);

		if (user == null || friend == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User or Friend IDs are invalid!")
				.addObject("userId", userId)
				.addObject("friendId", friendId)
				.build();
		}

		if (!user.isEnabled() || !friend.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User or Friend are not enabled!")
				.addObject("user", user)
				.addObject("friend", friend)
				.build();
		}

		if (!user.getFriendIds().contains(friendId) || !friend.getFriendIds().contains(userId)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Cannot unfriend someone who is not friends already!")
				.addObject("friend", friend)
				.addObject("user", user)
				.build();
		}

		if (userId == friendId) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Cannot unfriend yourself!")
				.addObject("user", user)
				.addObject("request", request)
				.build();
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
