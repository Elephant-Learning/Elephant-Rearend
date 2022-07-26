package me.elephantsuite.deck;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.deck.service.DeckService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.notification.Notification;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Deck {

	@Id
	@SequenceGenerator(name = "deck", sequenceName = "deck_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deck_sequence")
	private Long id;

	private int numberOfLikes = 0;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	@JsonBackReference
	private ElephantUser author;

	@OneToMany(mappedBy = "deck",  cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Card> cards = new ArrayList<>();

	private String name;

	private final LocalDateTime created = LocalDateTime.now();

	public Deck(List<Card> cards, ElephantUser author, String name) {
		this.cards = cards;
		this.author = author;
		this.name = name;
	}

	public void likeDeck() {
		numberOfLikes++;
	}

	public void resetTerms(Map<String, List<String>> newTerms, CardService cardService) {
		List<Card> cards = DeckService.convertToCards(newTerms, this);
		cardService.deleteAll(this.cards);
		this.cards = cards;
		cardService.saveAll(cards);
	}
}
