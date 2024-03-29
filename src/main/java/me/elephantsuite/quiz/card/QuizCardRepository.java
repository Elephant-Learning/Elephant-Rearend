package me.elephantsuite.quiz.card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QuizCardRepository extends JpaRepository<QuizCard, Long> {

    @Query(value = "DELETE FROM quiz_quiz_cards WHERE quiz_cards_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteCardRelation(long id);


    @Query(value = "SELECT * FROM quiz_card WHERE quiz_id = ?1", nativeQuery = true)
    @Transactional
    List<QuizCard> retrieveCardsByQuizId(long quizId);

}
