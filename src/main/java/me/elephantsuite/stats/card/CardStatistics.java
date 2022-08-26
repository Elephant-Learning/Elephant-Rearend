package me.elephantsuite.stats.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.stats.ElephantUserStatistics;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@ToString
@Entity
public class CardStatistics {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_statistics_generator")
	@SequenceGenerator(name = "card_statistics_generator", sequenceName = "card_statistics_sequence", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	private Long cardId;

	private int answeredRight;

	private int answeredWrong;

	public CardStatistics(long cardId) {
		this.cardId = cardId;
	}

	public void incrementAnsweredRight() {
		answeredRight++;
	}

	public void incrementAnsweredWrong() {
		answeredWrong++;
	}

}
