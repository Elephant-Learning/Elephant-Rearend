package me.elephantsuite.quiz.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface QuizCardRepository extends JpaRepository<QuizCard, Long> {

    @Query(value = "DELETE FROM quiz_cards WHERE cards_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    int deleteCardRelation(long id);

}
