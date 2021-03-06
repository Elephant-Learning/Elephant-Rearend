package me.elephantsuite.song;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/song")
@AllArgsConstructor
public class ElephantSongController {

	private final SongService songService;

	@PutMapping(path = "like")
	public Response likeSong(@RequestBody SongRequest request) {
		return songService.likeSong(request);
	}
}
