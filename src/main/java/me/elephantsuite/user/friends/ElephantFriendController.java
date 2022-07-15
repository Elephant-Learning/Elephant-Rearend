package me.elephantsuite.user.friends;

import java.util.Objects;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/friends")
@AllArgsConstructor
public class ElephantFriendController {

	private final ElephantFriendService service;


	@PutMapping
	public String addFriend(@RequestBody FriendRequest friendRequest) {
		return this.service.addFriend(friendRequest);
	}

	@DeleteMapping(path = "remove")
	public String removeFriend(@RequestBody FriendRequest friendRequest) {
		return this.service.removeFriend(friendRequest);
	}

}
