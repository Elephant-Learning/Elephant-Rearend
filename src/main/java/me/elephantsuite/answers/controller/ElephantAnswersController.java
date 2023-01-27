package me.elephantsuite.answers.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "answers")
@AllArgsConstructor
public class ElephantAnswersController {

	private final ElephantAnswersService service;

	@PostMapping(path = "create")
	public Response createAnswer(@RequestBody ElephantAnswersRequest.CreateAnswer request) {
		return service.createAnswer(request);
	}

	@PostMapping(path = "changeTitle")
	public Response changeTitle(@RequestBody ElephantAnswersRequest.ChangeTitle request) {
		return service.changeTitle(request, false);
	}

	@PostMapping(path = "changeDescription")
	public Response changeDescription(@RequestBody ElephantAnswersRequest.ChangeTitle request) {
		return service.changeTitle(request, true);
	}

	@PostMapping(path = "setAnswered")
	public Response setAnswered(@RequestParam("id") long answerId) {
		return service.setAnswered(answerId);
	}

	@PostMapping(path = "setTags")
	public Response setTags(@RequestBody ElephantAnswersRequest.SetTags request) {
		return service.setTags(request);
	}

	@PostMapping(path = "setUserTags")
	public Response setUserTags(@RequestBody ElephantAnswersRequest.SetUserTags request) {
		return service.setUserTags(request);
	}

	@PostMapping(path = "like")
	public Response likeAnswer(@RequestParam("id") long answerId) {
		return service.likeAnswer(answerId, true);
	}

	@PostMapping(path = "unlike")
	public Response unlikeAnswer(@RequestParam("id") long answerId) {
		return service.likeAnswer(answerId, false);
	}

	@DeleteMapping(path = "deleteAnswer")
	public Response deleteAnswer(@RequestParam("id") long answerId) {
		return service.deleteAnswer(answerId);
	}

	@PostMapping(path = "searchByName")
	public Response searchByName(@RequestParam("name") String name) {
		return service.searchByName(name);
	}

	@PostMapping(path = "editAnswer")
	public Response editAnswer(@RequestBody ElephantAnswersRequest.EditAnswer request) {
		return service.editAnswer(request);
	}

	@PostMapping(path = "createComment")
	public Response createComment(@RequestBody ElephantAnswersRequest.CreateComment request) {
		return service.createComment(request);
	}

	@PostMapping(path = "editComment")
	public Response editComment(@RequestBody ElephantAnswersRequest.EditComment request) {
		return service.editComment(request);
	}

	@PostMapping(path = "likeComment")
	public Response likeComment(@RequestParam("id") long commentId) {
		return service.likeComment(commentId, true);
	}

	@DeleteMapping(path = "deleteComment")
	public Response deleteComment(@RequestParam("id") long commentId) {
		return service.deleteComment(commentId);
	}

	@PostMapping(path = "createReply")
	public Response createReply(@RequestBody ElephantAnswersRequest.CreateReply request) {
		return service.createReply(request);
	}

	@PostMapping(path = "editReply")
	public Response editReply(@RequestBody ElephantAnswersRequest.EditReply request) {
		return service.editReply(request);
	}

	@DeleteMapping(path = "deleteReply")
	public Response deleteReply(@RequestParam("id") long replyId) {
		return service.deleteReply(replyId);
	}

	@PostMapping(path = "increaseAnswersScore")
	public Response increaseAnswersScore(@RequestBody ElephantAnswersRequest.IncreaseScore request) {
		return service.increaseScore(request);
	}


}
