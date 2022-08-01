package me.elephantsuite.backpack.service;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "backpack")
@AllArgsConstructor
public class BackpackController {

	private final BackpackService backpackService;

	@PutMapping(path = "addCard")
	public Response addCard(@RequestBody BackpackRequest.AddCard request) {
		return backpackService.addCard(request);
	}

	@DeleteMapping(path = "removeCard")
	public Response removeCard(@RequestBody BackpackRequest.RemoveCard request) {
		return backpackService.removeCard(request);
	}
}
