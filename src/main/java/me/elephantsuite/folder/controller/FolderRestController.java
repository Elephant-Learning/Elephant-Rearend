package me.elephantsuite.folder.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@PutMapping("addTimeline")
	public Response addTimeline(@RequestBody FolderRequest.AddTimeline request) {
		return service.addTimeline(request);
	}

	@DeleteMapping("removeTimeline")
	public Response removeTimeline(@RequestBody FolderRequest.AddTimeline request) {
		return service.removeTimeline(request);
	}

	@PostMapping("setName")
	public Response setFolderName(@RequestBody FolderRequest.SetName setName) {
		return service.setFolderName(setName);
	}

	@GetMapping("get")
	public Response getFolder(@RequestParam("id") long id) {
		return service.getFolderById(id);
	}

	@DeleteMapping("delete")
	public Response deleteFolder(@RequestParam("id") long id) {
		return service.deleteFolder(id);
	}
}
