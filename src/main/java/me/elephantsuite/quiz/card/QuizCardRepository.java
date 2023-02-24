package me.elephantsuite.quiz.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCardRepository extends JpaRepository<QuizCard, Long> {

}
