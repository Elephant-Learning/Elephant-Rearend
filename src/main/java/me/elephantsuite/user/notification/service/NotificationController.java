package me.elephantsuite.user.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@DeleteMapping(path = "delete")
	public String deleteNotification(@RequestParam("id") long id) {
		return notificationService.deleteNotification(id);
	}


}
