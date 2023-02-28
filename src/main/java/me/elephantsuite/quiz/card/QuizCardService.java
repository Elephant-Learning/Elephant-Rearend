package me.elephantsuite.quiz.card;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizCardService {
    private QuizCardRepository repository;

    public QuizCard save(QuizCard card) {
        return repository.save(card);
    }


}
