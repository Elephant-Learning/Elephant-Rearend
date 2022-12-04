package me.elephantsuite.answers.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "answers")
@AllArgsConstructor
public class ElephantAnswersController {

	private final ElephantAnswersService service;


}
