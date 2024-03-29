package me.elephantsuite.quiz;

import java.util.List;

import me.elephantsuite.quiz.card.QuizCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query(value = "SELECT q FROM QuizCard q WHERE q.quiz.id = ?1")
    @Transactional
    List<QuizCard> getCards(long quizId);
}
