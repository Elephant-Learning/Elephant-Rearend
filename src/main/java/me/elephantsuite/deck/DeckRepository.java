package me.elephantsuite.deck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Deck d WHERE d.id = ?1")
	int deleteDeckById(long id);
}
