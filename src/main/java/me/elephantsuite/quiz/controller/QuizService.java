package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.deck.controller.DeckService;
import me.elephantsuite.quiz.Quiz;
import me.elephantsuite.quiz.QuizRepository;
import me.elephantsuite.quiz.QuizRepositoryService;
import me.elephantsuite.quiz.card.QuizCard;
import me.elephantsuite.quiz.card.QuizCardService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
public class QuizService {

    private ElephantUserService userService;

    private QuizRepositoryService quizService;

    private QuizCardService quizCardService;

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
        Map<String, List<String>> terms = req.getCards();

        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        if (RegistrationService.isInvalidName(name) || RegistrationService.isInvalidName(desc)) {
            throw new InvalidTagInputException(name, desc);
        }

        String error = DeckService.hasInvalidTag(terms);

        if (error != null) {
            throw new InvalidTagInputException(error);
        }

        Quiz quiz = new Quiz(name, desc, user);

        List<QuizCard> cards = convertToCards(terms, quiz, quizCardService);
        quiz.setCards(cards);

        user.getQuizzes().add(quiz);

        quiz = quizService.save(quiz);
        user = userService.saveUser(user);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Created Quiz!")
                .addObject("quiz", quiz)
                .build();
    }

    public Response setQuizCards(QuizRequest.SetQuizCards req) {
        long id = req.getQuizId();

        Quiz quiz = ResponseUtil.checkEntityValid(id, repository, InvalidIdType.QUIZ);
        List<QuizCard> cards = convertToCards(req.getNewTerms(), quiz, quizCardService);
        quiz.setCards(cards);

        quiz = quizService.save(quiz);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Set Quiz Terms!")
                .addObject("quiz", quiz)
                .build();

    }

    public static List<QuizCard> convertToCards(Map<String, List<String>> cardsMap, Quiz quiz, QuizCardService cardService) {
        List<QuizCard> cards = new ArrayList<>();

        cardsMap.forEach((s, strings) -> {
            QuizCard card = new QuizCard(s, strings, quiz);
            cards.add(card);
            cardService.save(card);
        });

        return cards;
    }
}