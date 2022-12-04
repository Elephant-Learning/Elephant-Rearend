package me.elephantsuite.answers.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.answers.ElephantAnswerRepositoryService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantAnswersService {

	private final ElephantAnswerRepositoryService service;
}
