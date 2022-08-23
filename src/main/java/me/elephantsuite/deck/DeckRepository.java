package me.elephantsuite.deck;

import java.util.List;

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

	@Transactional
	@Query(value = "SELECT * FROM deck", nativeQuery = true)
	List<Deck> getAllDecks();

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM deck_shared_users_ids WHERE deck_shared_users_ids.shared_users_ids = ?1", nativeQuery = true)
	void deleteUserFromSharedDecks(long userId);

	@Transactional
	@Query(value = "SELECT * FROM deck WHERE deck.elephant_user_id = ?1", nativeQuery = true)
	List<Deck> getDecksByUserId(long id);
}
