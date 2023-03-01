package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "quiz")
@AllArgsConstructor
public class QuizController {

    private QuizService quizService;

    @PostMapping(path = "create")
    public Response createQuiz(@RequestBody QuizRequest.CreateQuiz req) {
        return quizService.createQuiz(req);
    }

    @PostMapping(path = "editDetails")
    public Response editDetails(@RequestBody QuizRequest.EditNameAndDescription req) {
        return quizService.editQuiz(req);
    }

    @PostMapping(path = "setQuizCards")
    public Response setQuizCards(@RequestBody QuizRequest.SetQuizCards req) {
        return quizService.setQuizCards(req);
    }

    @DeleteMapping(path = "delete")
    public Response deleteQuiz(@RequestParam("id") long quizId) {
        return quizService.deleteQuiz(quizId);
    }

    @DeleteMapping(path = "deleteCard")
    public Response deleteQuizCard(@RequestParam("id") long cardId) {
        return quizService.deleteQuizCard(cardId);
    }
}
