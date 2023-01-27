package me.elephantsuite.answers;

import java.util.List;

import me.elephantsuite.deck.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ElephantAnswerRepository extends JpaRepository<ElephantAnswer, Long> {
	@Transactional
	@Query(value = "SELECT * FROM elephant_answer", nativeQuery = true)
	List<ElephantAnswer> getAllAnswers();
}
