package me.elephantsuite.song;

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
public class SongService {

	private final ElephantUserService service;

	public Response likeSong(SongRequest request) {
		long userId = request.getUserId();
		String songName = request.getSongName();

		ElephantUser user = service.getUserById(userId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
				.addObject("request", request)
				.build();
		}

		user.getLikedSongs().add(songName);

		user = this.service.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Liked Song to user!")
			.addObject("user", user)
			.build();
	}
}
