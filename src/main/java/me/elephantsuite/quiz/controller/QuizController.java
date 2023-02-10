package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "quiz")
@AllArgsConstructor
public class QuizController {

    private QuizService quizService;

    @PostMapping(path = "create")
    public Response createQuiz(@RequestBody QuizRequest.CreateQuiz req) {
        return quizService.createQuiz(req);
    }
}
