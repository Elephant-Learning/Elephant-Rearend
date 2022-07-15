package me.elephantsuite.user.friends;

import java.util.Objects;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantFriendService {

	private final ElephantUserService userService;



	public String addFriend(FriendRequest request) {

		long userId = request.getUserId();
		long friendId = request.getFriendId();

		ElephantUser user = this.userService.getUserById(userId);

		ElephantUser friend = this.userService.getUserById(friendId);

		if (user == null || friend == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User or Friend IDs are invalid!")
				.addValue(jsonObject -> jsonObject.addProperty("userId", userId))
				.addValue(jsonObject -> jsonObject.addProperty("friendId", friendId))
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

		//TODO add notification and actual "request"s
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

	public String removeFriend(FriendRequest request) {
		long userId = request.getUserId();
		long friendId = request.getFriendId();

		ElephantUser user = this.userService.getUserById(userId);

		ElephantUser friend = this.userService.getUserById(friendId);

		if (user == null || friend == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User or Friend IDs are invalid!")
				.addValue(jsonObject -> jsonObject.addProperty("userId", userId))
				.addValue(jsonObject -> jsonObject.addProperty("friendId", friendId))
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
