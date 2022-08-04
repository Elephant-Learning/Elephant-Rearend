package me.elephantsuite.folder.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "folder")
public class FolderRestController {

	private final FolderService service;

	@PostMapping("create")
	public Response createFolder(@RequestBody FolderRequest.CreateFolder createFolder) {
		return service.createFolder(createFolder);
	}

	@PutMapping("addDeck")
	public Response addDeck(@RequestBody FolderRequest.AddDeck addDeck) {
		return service.addDeck(addDeck);
	}

	@DeleteMapping("removeDeck")
	public Response removeDeck(@RequestBody FolderRequest.RemoveDeck removeDeck) {
		return service.removeDeck(removeDeck);
	}
}
