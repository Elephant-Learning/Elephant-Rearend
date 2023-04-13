package me.elephantsuite.quiz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class QuizRepositoryService {

    private QuizRepository repository;

    @Transactional
    public Quiz save(Quiz quiz) {
        return repository.save(quiz);
    }

    public Quiz getById(long id) {
        if (repository.existsById(id)) {
            return repository.getReferenceById(id);
        }

        return null;
    }
}
