package me.elephantsuite.user.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/notifications")
@AllArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@PostMapping(path = "sendFriendRequest")
	public String sendFriendRequest(@RequestBody NotificationRequest.FriendRequest request) {
		return notificationService.sendFriendRequest(request);
	}

	@PostMapping(path = "sendLikedDeck")
	public String sendLikedDeckNotification(@RequestBody NotificationRequest.LikedDeckRequest request) {
		return notificationService.sendLikedDeck(request);
	}

	@PostMapping(path = "sendSharedDeck")
	public String sendSharedDeck(@RequestBody NotificationRequest.ShareDeckRequest request) {
		return notificationService.sendSharedDeck(request);
	}

	@DeleteMapping(path = "delete")
	public String deleteNotification(@RequestParam("id") long id) {
		return notificationService.deleteNotification(id);
	}


}
