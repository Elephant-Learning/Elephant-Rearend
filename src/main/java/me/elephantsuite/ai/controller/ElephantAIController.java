package me.elephantsuite.ai.controller;

import java.io.IOException;

import lombok.AllArgsConstructor;
import me.elephantsuite.answers.controller.ElephantAnswersRequest;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "answers")
@AllArgsConstructor
public class ElephantAIController {

	private ElephantAIService elephantAIService;

	@PostMapping(path = "sendMessage")
	public Response createAnswer(@RequestBody ElephantAIRequest.SendMessage request) {
		return elephantAIService.sendMessage(request);
	}
}
