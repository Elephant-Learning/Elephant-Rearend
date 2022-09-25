package me.elephantsuite.user.friends;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "friends")
@AllArgsConstructor
public class ElephantFriendController {

	private final ElephantFriendService service;


	@PutMapping(path = "add")
	public Response addFriend(@RequestBody FriendRequest friendRequest) {
		return this.service.addFriend(friendRequest);
	}

	@DeleteMapping(path = "remove")
	public Response removeFriend(@RequestBody FriendRequest friendRequest) {
		return this.service.removeFriend(friendRequest);
	}

}
