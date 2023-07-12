package me.elephantsuite.stats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.quiz.card.QuizCard;
import me.elephantsuite.stats.card.CardStatistics;
import me.elephantsuite.stats.medal.Medal;
import me.elephantsuite.stats.medal.MedalService;
import me.elephantsuite.stats.quiz_card.QuizCardStatistics;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quizCardStatistics"})
@NoArgsConstructor
@ToString
@Entity
public class ElephantUserStatistics {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elephant_user_statistics_generator")
	@SequenceGenerator(name = "elephant_user_statistics_generator", sequenceName = "elephant_user_statistics_sequence", allocationSize = 1)
	private Long id;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	@JsonBackReference
	private ElephantUser user;

	private int daysStreak = 0;

	private double usageTime = 0;

	private LocalDateTime lastLoggedIn = LocalDateTime.now();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> recentlyViewedDeckIds = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> recentlyViewedTimelineIds = new ArrayList<>();

	@OneToMany(mappedBy = "userStatistics", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<Medal> medals = new ArrayList<>();


	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "elephant_user_statistics_card_statistics_mapping", joinColumns = {@JoinColumn(name = "elephant_user_statistics_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "card_statistics_id")})
	@MapKeyJoinColumn(name = "card_id")
	private Map<Card, CardStatistics> cardStatistics = new HashMap<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "elephant_user_statistics_quiz_card_statistics_mapping", joinColumns = {@JoinColumn(name = "elephant_user_statistics_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "quiz_card_statistics_id")})
	@MapKeyJoinColumn(name = "quiz_card_id")
//	@JsonIgnore
	private Map<QuizCard, QuizCardStatistics> quizCardStatistics = new HashMap<>();

	public ElephantUserStatistics(ElephantUser user) {
		this.user = user;
	}

	public void incrementDaysStreak(MedalService medalService) {
		if (this.lastLoggedIn.getDayOfYear() == LocalDateTime.now().getDayOfYear()) {
			return;
		}


		if ((this.lastLoggedIn.getDayOfYear() + 1 == LocalDateTime.now().getDayOfYear()) && this.lastLoggedIn.getYear() == LocalDateTime.now().getYear()) {
			daysStreak += 1;
			medalService.updateLoginMedal(this);
		} else {
			daysStreak = 0;
		}
	}

	public void increaseUsageTime(double usageTime) {
		this.usageTime += usageTime;
	}

	public void resetLoginDate() {
		this.lastLoggedIn = LocalDateTime.now();
	}
}
