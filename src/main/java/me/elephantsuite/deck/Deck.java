package me.elephantsuite.deck;

import java.time.LocalDateTime;
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
import me.elephantsuite.user.ElephantUser;
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

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private Map<String, DefinitionList> terms;

	private int numberOfLikes = 0;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	@JsonBackReference
	private ElephantUser author;

	private String name;

	private final LocalDateTime created = LocalDateTime.now();

	public Deck(Map<String, List<String>> terms, ElephantUser author, String name) {
		this.terms = convertToDefinitionList(terms);
		this.author = author;
		this.name = name;
	}

	public void likeDeck() {
		numberOfLikes++;
	}

	private static Map<String, DefinitionList> convertToDefinitionList(Map<String, List<String>> stringListMap) {
		Map<String, DefinitionList> definitionListMap = new HashMap<>();

		stringListMap.forEach((s, strings) -> {
			definitionListMap.put(s, new DefinitionList(strings));
		});

		return definitionListMap;
	}

	public void putTerms(String s, List<String> strings) {
		this.terms.put(s, new DefinitionList(strings));
	}

	public void removeTerms(String key, List<String> values) {
		this.terms.remove(key, new DefinitionList(values));
	}
}
