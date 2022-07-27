package me.elephantsuite.deck.card;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.elephantsuite.deck.Deck;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_generator")
	@SequenceGenerator(name = "card_generator", sequenceName = "card_sequence", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> definitions = new ArrayList<>();

	private String term;

	@ManyToOne(fetch = FetchType.EAGER,  cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "deck_id", foreignKey = @ForeignKey(name = "deck_id"))
	@JsonBackReference
	private Deck deck;

	public Card(String term, List<String> definitions, Deck deck)  {
		this.term = term;
		this.definitions = definitions;
		this.deck = deck;
	}

}
