package me.elephantsuite.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.stats.ElephantUserStatistics;
import me.elephantsuite.user.notification.Notification;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
//eventually what we want to create based on a registration request
public class ElephantUser {

	@Id
	@SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private ElephantUserType type;

	private boolean locked = false;

	private boolean enabled = false;

	private int pfpId;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "elephant_user_id")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> friendIds = new ArrayList<>();

	@OneToMany(mappedBy = "recipient",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(mappedBy = "author",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Deck> decks = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "elephant_user_id")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> likedDecksIds = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "elephant_user_id")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<String> likedSongs = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "statistics_id")
	private ElephantUserStatistics statistics;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private ConfirmationToken token;

	public ElephantUser(String firstName, String lastName, String email, String password, ElephantUserType type, Integer pfpId) {
		this.firstName = Objects.requireNonNull(firstName);
		this.email = Objects.requireNonNull(email);
		this.lastName = Objects.requireNonNull(lastName);
		this.password = Objects.requireNonNull(password);
		this.type = Objects.requireNonNull(type);
		//for whatever reason if pfpId is null just set it to something random
		this.pfpId = pfpId == null ? new Random().nextInt(48) : pfpId;
		this.statistics = new ElephantUserStatistics(this);
	}

}
