package me.elephantsuite.song;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
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
public class SongService {

	private final ElephantUserService service;

	public Response likeSong(SongRequest request) {
		long userId = request.getUserId();
		String songName = request.getSongName();

		ElephantUser user = ResponseUtil.checkUserValid(userId, service);

		user.getLikedSongs().add(songName);

		user = this.service.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Liked Song to user!")
			.addObject("user", user)
			.build();
	}

	public Response unlikeSong(SongRequest request) {
		long userId = request.getUserId();
		String songName = request.getSongName();

		ElephantUser user = ResponseUtil.checkUserValid(userId, service);

		if (!user.isEnabled()) {
			return ResponseUtil.getFailureResponse("User not enabled!", request);
		}

		user.getLikedSongs().remove(songName);

		user = this.service.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Liked Song to user!")
			.addObject("user", user)
			.build();
	}
}
