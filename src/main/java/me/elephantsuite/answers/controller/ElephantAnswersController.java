package me.elephantsuite.answers.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	public Response setAnswered(@RequestBody ElephantAnswersRequest.SetAnswerAnswered request) {
		return service.setAnswered(request);
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
	public Response likeAnswer(@RequestBody ElephantAnswersRequest.LikeAnswer request) {
		return service.likeAnswer(request, true);
	}

	@PostMapping(path = "unlike")
	public Response unlikeAnswer(@RequestBody ElephantAnswersRequest.LikeAnswer request) {
		return service.likeAnswer(request, false);
	}

	@DeleteMapping(path = "deleteAnswer")
	public Response deleteAnswer(@RequestParam("id") long answerId) {
		return service.deleteAnswer(answerId);
	}

	@PostMapping(path = "searchByName")
	public Response searchByName(@RequestParam("name") String name) {
		return service.searchByName(name);
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
	public Response likeComment(@RequestBody ElephantAnswersRequest.LikeComment request) {
		return service.likeComment(request, true);
	}

	@PostMapping(path = "unlikeComment")
	public Response unlikeComment(@RequestBody ElephantAnswersRequest.LikeComment request) {
		return service.likeComment(request, false);
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

	@GetMapping(path = "getById")
	public Response getById(@RequestParam("id") long id) {
		return service.getById(id);
	}

	@GetMapping(path = "getCommentById")
	public Response getCommentById(@RequestParam("id") long id) {
		return service.getCommentById(id);
	}

	@GetMapping(path = "getAnswersForUser")
	public Response getAnswersForUser(@RequestParam("userId") long userId) {
		return service.getAnswersForUser(userId);
	}
}
