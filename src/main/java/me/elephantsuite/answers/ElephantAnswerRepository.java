package me.elephantsuite.answers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElephantAnswerRepository extends JpaRepository<ElephantAnswer, Long> {
}
