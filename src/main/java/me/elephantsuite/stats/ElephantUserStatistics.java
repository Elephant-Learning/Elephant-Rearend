package me.elephantsuite.stats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.stats.card.CardStatistics;
import me.elephantsuite.user.ElephantUser;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@ToString
@Entity
public class ElephantUserStatistics {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elephant_user_statistics_generator")
	@SequenceGenerator(name = "elephant_user_statistics_generator", sequenceName = "elephant_user_statistics_sequence", allocationSize = 1)
	private Long id;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JsonBackReference
	private ElephantUser user;

	private int daysStreak = 0;

	private double usageTime = 0;

	private LocalDateTime lastLoggedIn = LocalDateTime.now();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "elephant_user_statistics_card_statistics_mapping", joinColumns = {@JoinColumn(name = "elephant_user_statistics_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "card_statistics_id")})
	@MapKeyJoinColumn(name = "card_id")
	private Map<Card, CardStatistics> cardStatistics = new HashMap<>();

	public ElephantUserStatistics(ElephantUser user) {
		this.user = user;
	}

	public void incrementDaysStreak() {
		if ((this.lastLoggedIn.getDayOfYear() + 1 == LocalDateTime.now().getDayOfYear()) && this.lastLoggedIn.getYear() == LocalDateTime.now().getYear()) {
			daysStreak += 1;
		}
	}

	public void increaseUsageTime(double usageTime) {
		this.usageTime += usageTime;
	}

	public void resetLoginDate() {
		this.lastLoggedIn = LocalDateTime.now();
	}
}
