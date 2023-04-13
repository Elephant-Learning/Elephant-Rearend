package me.elephantsuite.quiz.card;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class QuizCardService {
    private QuizCardRepository repository;

    public QuizCard save(QuizCard card) {
        return repository.save(card);
    }


    public void delete(QuizCard quizCard) {
        repository.delete(quizCard);
    }

    public void saveAll(List<QuizCard> quizCards) {
        repository.saveAll(quizCards);
    }
}
