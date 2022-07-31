package me.elephantsuite.user.notification.service;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "notifications")
@AllArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@PostMapping(path = "sendFriendRequest")
	public Response sendFriendRequest(@RequestBody NotificationRequest.FriendRequest request) {
		return notificationService.sendFriendRequest(request);
	}

	@PostMapping(path = "sendLikedDeck")
	public Response sendLikedDeckNotification(@RequestBody NotificationRequest.LikedDeckRequest request) {
		return notificationService.sendLikedDeck(request);
	}

	@PostMapping(path = "sendSharedDeck")
	public Response sendSharedDeck(@RequestBody NotificationRequest.ShareDeckRequest request) {
		return notificationService.sendSharedDeck(request);
	}

	@DeleteMapping(path = "delete")
	public Response deleteNotification(@RequestParam("id") long id) {
		return notificationService.deleteNotification(id);
	}


}
