package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.quiz.Quiz;
import me.elephantsuite.quiz.QuizRepository;
import me.elephantsuite.quiz.QuizRepositoryService;
import me.elephantsuite.registration.RegistrationService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class QuizService {

    private ElephantUserService userService;

    private QuizRepositoryService quizService;

    private QuizRepository repository;

    public Response editQuiz(QuizRequest.EditNameAndDescription req) {
        String name = req.getName();
        String desc = req.getDescription();
        long id = req.getQuizId();

        Quiz quiz = ResponseUtil.checkEntityValid(id, repository, InvalidIdType.QUIZ);

        quiz.setDescription(desc);
        quiz.setName(name);

        quiz = quizService.save(quiz);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Edited Quiz!")
                .addObject("quiz", quiz)
                .build();




    }

    public Response createQuiz(QuizRequest.CreateQuiz req) {
        String name = req.getName();
        String desc = req.getDescription();
        long userId = req.getUserId();

        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        if (RegistrationService.isInvalidName(name) || RegistrationService.isInvalidName(desc)) {
            throw new InvalidTagInputException(name, desc);
        }

        Quiz quiz = new Quiz(name, desc, user);
        user.getQuizzes().add(quiz);

        quiz = quizService.save(quiz);
        user = userService.saveUser(user);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Created Quiz!")
                .addObject("quiz", quiz)
                .build();
    }
}