package me.elephantsuite.user.friends;

import java.util.Objects;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/friends")
@AllArgsConstructor
public final class ElephantFriendController {

	private final ElephantFriendService service;


	@PostMapping
	public String addFriend(@RequestBody FriendRequest friendRequest) {
		return this.service.addFriend(friendRequest);
	}

	@PostMapping(path = "remove")
	public String removeFriend(@RequestBody FriendRequest friendRequest) {
		return this.service.removeFriend(friendRequest);
	}

}
