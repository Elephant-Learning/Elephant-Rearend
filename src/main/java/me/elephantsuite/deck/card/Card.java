package me.elephantsuite.deck.card;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.elephantsuite.backpack.Backpack;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.response.json.CardSerializer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonSerialize(using = CardSerializer.class)
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

	//@ManyToOne(fetch = FetchType.EAGER,  cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	//@JsonBackReference
	//private Deck deck;

	private String deckName;

	public Card(String term, List<String> definitions, Deck deck)  {
		this.term = term;
		this.definitions = definitions;
		this.deckName = deck.getName();
	}

	@Override
	public String toString() {
		return this.term + " - " + this.id.toString();
	}
}
