package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
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
import me.elephantsuite.response.exception.InvalidIdException;
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

        Quiz quiz = getQuizById(req.getQuizId());

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
        quiz.setQuizCards(cards);

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

        Quiz quiz = getQuizById(req.getQuizId());
        quiz.getQuizCards().forEach(quizCard -> quizCardStatisticsService.deleteCardData(quizCard.getId()));
        quiz.getQuizCards().clear();
        List<QuizCard> cards = convertToCards(req.getNewTerms(), quiz);
        quiz.getQuizCards().addAll(cards);


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
            cards.add(card);
            card = quizCardService.save(card);

        });

        registerCardStatistics(cards, quiz.getUser());

        return cards;
    }

    public Response deleteQuiz(long quizId) {
        Quiz quiz = getQuizById(quizId);
        quiz.getQuizCards().forEach(quizCard -> quizCardStatisticsService.deleteCardData(quizCard.getId()));
        quiz.getUser().getQuizzes().remove(quiz);
        quiz.getQuizCards().clear();
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
        quizCardStatisticsService.deleteCardData(cardId);
        cardRepository.delete(card);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Deleted Card!")
                .build();
    }

    public Response importDeck(QuizRequest.ImportDeck req) {
        Quiz quiz = getQuizById(req.getQuizId());
        Deck deck = ResponseUtil.checkEntityValid(req.getDeckId(), deckRepository, InvalidIdType.DECK);



        List<QuizCard> quizCards = convertToCards(convertCardsToMap(deck.getCards()), quiz);

        quiz.getQuizCards().addAll(quizCards);
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
        Quiz quiz = getQuizById(req.getQuizId());
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

        quizCardStatisticsService.save(user.getElephantUserStatistics().getQuizCardStatistics().get(card));

        statisticsService.save(user.getElephantUserStatistics());

        user = userService.saveUser(user);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Set Card as " + (b ? "Correct" : "Incorrect") + "!")
                .addObject("stats", user.getElephantUserStatistics().getQuizCardStatistics().get(card))
                .build();
    }

    private QuizCardStatistics randomizeQuestionTypesAndGetStatistics(QuizCard iCard, ElephantUser user, boolean randomize) {
        if (randomize) {
            iCard.setType(QuestionType.values()[(RANDOM.nextInt(QuestionType.values().length))]);
            iCard = quizCardService.save(iCard);
        }
        Map<QuizCard, QuizCardStatistics> statMap = user.getElephantUserStatistics().getQuizCardStatistics();
        QuizCardStatistics statistics = statMap.get(iCard);

        return statistics;
    }

    private static <T> void swap(List<T> list, int i1, int i2) {
        T temp = list.get(i1);
        list.set(i1, list.get(i2));
        list.set(i2, temp);
    }

    public Response getQuestions(long userId, long quizId) {
        Quiz quiz = getQuizById(quizId);
        ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

        List<QuizCard> cards = new ArrayList<>(cardRepository.retrieveCardsByQuizId(quizId));

        Collections.shuffle(cards);

        List<Long> alrSortedNums = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            QuizCardStatistics statistics = randomizeQuestionTypesAndGetStatistics(cards.get(i), user, true);
            if (alrSortedNums.contains(statistics.getQuizCardId())) {
                continue;
            }
            if (statistics.isAnsweredCorrectly()) {
                for (int j = cards.size() - 1; j >= 0; j--) {
                    QuizCardStatistics jStats = randomizeQuestionTypesAndGetStatistics(cards.get(j), user, false);
                    if (jStats.getQuizCardId().equals(statistics.getQuizCardId())) {
                        continue;
                    }
                    if (!jStats.isAnsweredCorrectly()) {
                        //ElephantBackendApplication.LOGGER.info("Cards Before: " + cards);
                        swap(cards, i, j);
                        //ElephantBackendApplication.LOGGER.info("Cards After: " + cards);
                        break;
                    }
                }

                alrSortedNums.add(statistics.getQuizCardId());
            }
        }
        /*

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



        finalCards.addAll(rightCards);

         */


        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Retrieved Questions!")
                .addObject("questions", cards)
                .build();

    }

    private void registerCardStatistics(List<QuizCard> cards, ElephantUser user) {
        for (QuizCard card : cards) {
            if (!user.getElephantUserStatistics().getQuizCardStatistics().containsKey(card)) {
                QuizCardStatistics cardStatistics = new QuizCardStatistics(card.getId());
                cardStatistics = quizCardStatisticsService.save(cardStatistics);
                user.getElephantUserStatistics().getQuizCardStatistics().put(card, cardStatistics);
                statisticsService.save(user.getElephantUserStatistics());
            }
        }
    }

    @Transactional
    public Quiz getQuizById(long id) {
        if (repository.existsById(id)) {
            Quiz quiz = repository.getReferenceById(id);
            List<QuizCard> cards = repository.getCards(id);

            if (!cards.equals(quiz.getQuizCards())) {
                quiz.getQuizCards().clear();
                quiz.getQuizCards().addAll(cards);
                quiz = quizService.save(quiz);
            }
            return quiz;
        }

        throw new InvalidIdException(id, InvalidIdType.QUIZ);
    }

    public Response getById(long id) {
        Quiz quiz = getQuizById(id);

        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.SUCCESS, "Retrieved Quiz!")
                .addObject("quiz", quiz)
                .build();
    }
}