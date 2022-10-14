package me.elephantsuite.deck.card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM card_definitions WHERE card_definitions.card_id = ?1", nativeQuery = true)
	int deleteCardDefinitionsByCardID(long id);

	@Transactional
	@Modifying
	@Query("DELETE FROM Card c WHERE c.id = ?1")
	int deleteCardByID(long id);

	@Transactional
	@Query(value = "SELECT backpack_id FROM backpack_cards WHERE backpack_cards.cards_id = ?1", nativeQuery = true)
	List<Long> backpackIdsMatchWithCard(long cardId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM deck_cards WHERE deck_cards.cards_id = ?1", nativeQuery = true)
	int deleteCardsFromDeckTable(long cardId);
}
