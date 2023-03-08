package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepository;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.controller.DeckService;
import me.elephantsuite.quiz.QuestionType;
import me.elephantsuite.quiz.Quiz;
import me.elephantsuite.quiz.QuizRepository;
import me.elephantsuite.quiz.QuizRepositoryService;
import me.elephantsuite.quiz.card.QuizCard;
import me.elephantsuite.quiz.card.QuizCardRepository;
import me.elephantsuite.quiz.card.QuizCardService;
import me.elephantsuite.registration.RegistrationService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.stats.ElephantUserStatisticsRepositoryService;
import me.elephantsuite.stats.quiz_card.QuizCardStatistics;
import me.elephantsuite.stats.quiz_card.QuizCardStatisticsService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class QuizService {

    private ElephantUserService userService;

    private QuizRepositoryService quizService;

    private QuizCardService quizCardService;

    private QuizRepository repository;

    private QuizCardRepository cardRepository;

    private DeckRepository deckRepository;

    private ElephantUserStatisticsRepositoryService statisticsService;

    private QuizCardStatisticsService quizCardStatisticsService;

    private static final Random RANDOM = new Random();

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

        List<QuizCard> cards = convertToCards(terms, quiz);
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
        quiz.getCards().clear();
        List<QuizCard> cards = convertToCards(req.getNewTerms(), quiz);
        quiz.getCards().addAll(cards);


        quiz = quizService.save(quiz);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Set Quiz Terms!")
                .addObject("quiz", quiz)
                .build();

    }

    public List<QuizCard> convertToCards(Map<String, List<String>> cardsMap, Quiz quiz) {
        List<QuizCard> cards = new ArrayList<>();

        cardsMap.forEach((s, strings) -> {
            QuizCard card = new QuizCard(s, new ArrayList<>(strings), quiz, QuestionType.values()[(RANDOM.nextInt(QuestionType.values().length))]); // Create a new ArrayList for the "definitions" collection
            ElephantUser user = quiz.getUser();
            if (!user.getElephantUserStatistics().getQuizCardStatistics().containsKey(card)) {
                QuizCardStatistics statistics = new QuizCardStatistics(card.getId());
                statistics = quizCardStatisticsService.save(statistics);
                user.getElephantUserStatistics().getQuizCardStatistics().put(card, statistics);
                statisticsService.save(user.getElephantUserStatistics());
            }
            cards.add(card);
            quizCardService.save(card);
        });

        return cards;
    }

    public Response deleteQuiz(long quizId) {
        Quiz quiz = ResponseUtil.checkEntityValid(quizId, repository, InvalidIdType.QUIZ);

        quiz.getUser().getQuizzes().remove(quiz);
        quiz.getCards().clear();
        repository.delete(quiz);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Deleted Quiz!")
                .build();
    }

    public Response deleteQuizCard(long cardId) {
        QuizCard card = ResponseUtil.checkEntityValid(cardId, cardRepository, InvalidIdType.QUIZ_CARD);
        card.setQuiz(null);
        cardRepository.deleteCardRelation(card.getId());
        cardRepository.delete(card);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Deleted Card!")
                .build();
    }

    public Response importDeck(QuizRequest.ImportDeck req) {
        Quiz quiz = ResponseUtil.checkEntityValid(req.getQuizId(), repository, InvalidIdType.QUIZ_CARD);
        Deck deck = ResponseUtil.checkEntityValid(req.getDeckId(), deckRepository, InvalidIdType.DECK);



        List<QuizCard> quizCards = convertToCards(convertCardsToMap(deck.getCards()), quiz);

        quiz.getCards().addAll(quizCards);
        quizService.save(quiz);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Imported Cards from Deck!")
                .addObject("quiz", quiz)
                .build();
    }

    private static Map<String, List<String>> convertCardsToMap(List<Card> cards) {
        Map<String, List<String>> map = new HashMap<>();

        cards.forEach(card -> {
            map.put(card.getTerm(), card.getDefinitions());
        });

        return map;
    }

    public Response setTimeLimit(QuizRequest.SetTimeLimit req) {
        Quiz quiz = ResponseUtil.checkEntityValid(req.getQuizId(), repository, InvalidIdType.QUIZ);
        double timeLimit = req.getNewTimeLimit();

        quiz.setTimeLimit(timeLimit);

        quiz = quizService.save(quiz);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Set Time Limit!")
                .addObject("quiz", quiz)
                .build();
    }

    public Response setCardAsIncorrect(QuizRequest.SetCardAsIncorrect req, boolean b) {
        ElephantUser user = ResponseUtil.checkUserValid(req.getUserId(), userService);
        QuizCard card = ResponseUtil.checkEntityValid(req.getQuizCardId(), cardRepository, InvalidIdType.QUIZ_CARD);

        if (!user.getElephantUserStatistics().getQuizCardStatistics().containsKey(card)) {
            QuizCardStatistics statistics = new QuizCardStatistics(card.getId());
            statistics = quizCardStatisticsService.save(statistics);
            user.getElephantUserStatistics().getQuizCardStatistics().put(card, statistics);
        }

        user.getElephantUserStatistics().getQuizCardStatistics().get(card).setAnsweredCorrectly(b);

        statisticsService.save(user.getElephantUserStatistics());

        user = userService.saveUser(user);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Set Card as Incorrect!")
                .addObject("user", user)
                .build();
    }

    public Response getQuestions(long userId, long quizId) {
        Quiz quiz = ResponseUtil.checkEntityValid(quizId, repository, InvalidIdType.QUIZ);
        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        List<QuizCard> cards = new ArrayList<>(quiz.getCards());

        List<QuizCard> finalCards = new ArrayList<>();

        List<QuizCard> rightCards = new ArrayList<>();

        cards.forEach(quizCard -> {
            quizCard.setType(QuestionType.values()[(RANDOM.nextInt(QuestionType.values().length))]);
            quizCard = quizCardService.save(quizCard);
            Map<QuizCard, QuizCardStatistics> statMap = user.getElephantUserStatistics().getQuizCardStatistics();
            QuizCardStatistics statistics = statMap.get(quizCard);

            if (statistics != null) {
                if (!statistics.isAnsweredCorrectly()) {
                    finalCards.add(quizCard);
                } else {
                    rightCards.add(quizCard);
                }
            } else {
                rightCards.add(quizCard);
            }
        });

        Collections.shuffle(rightCards);

        finalCards.addAll(rightCards);


        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Retrieved Questions!")
                .addObject("question", finalCards)
                .build();

    }
}