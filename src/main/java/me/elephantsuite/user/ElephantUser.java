package me.elephantsuite.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.answers.ElephantAnswer;
import me.elephantsuite.backpack.Backpack;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.folder.Folder;
import me.elephantsuite.quiz.Quiz;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.stats.ElephantUserStatistics;
import me.elephantsuite.timeline.Timeline;
import me.elephantsuite.user.config.UserConfig;
import me.elephantsuite.user.notification.Notification;
import me.elephantsuite.user.password.ResetPasswordToken;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class ElephantUser {

	@Id
	@SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
	private Long id;

	private String firstName;

	private String lastName;

	@Column(unique = true)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private ElephantUserType type;

	private boolean locked = false;

	private boolean enabled = false;

	private boolean newUser = true;

	private boolean agreedToTos = false;

	private int pfpId;

	private int countryCode;

	private Integer elephantAnswersScore = 0;

	@ElementCollection
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> elephantAnswersLiked = new ArrayList<>();

	@ElementCollection
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> commentsLiked = new ArrayList<>();

	@ElementCollection
	@Fetch(FetchMode.SUBSELECT)
	private List<Integer> elephantAnswersTags = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "elephant_user_id")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> friendIds = new ArrayList<>();

	@ElementCollection
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> sharedTimelineIds = new ArrayList<>();

	@OneToMany(mappedBy = "recipient",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(mappedBy = "author",  cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Deck> decks = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "elephant_user_id")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> likedDecksIds = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> likedTimelineIds = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "elephant_user_id")
	@Fetch(value = FetchMode.SUBSELECT)
	private List<String> likedSongs = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> invitedUsers = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Long> sharedDeckIds = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<Folder> folders = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<Timeline> timelines = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<ElephantAnswer> answers = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<Quiz> quizzes = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private ElephantUserStatistics elephantUserStatistics = new ElephantUserStatistics(this);

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private ConfirmationToken confirmationToken;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private ResetPasswordToken resetPasswordToken;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "backpack_id")
	private Backpack backpack = new Backpack(this);

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private UserConfig config = new UserConfig(this);

	public ElephantUser(String firstName, String lastName, String email, String password, ElephantUserType type, Integer countryCode, Integer pfpId) {
		this.firstName = Objects.requireNonNull(firstName, "firstName cannot be null");
		this.email = Objects.requireNonNull(email, "email cannot be null");
		this.lastName = Objects.requireNonNull(lastName, "lastName cannot be null");
		this.password = Objects.requireNonNull(password, "password cannot be null");
		this.type = Objects.requireNonNull(type, "type cannot be null");
		this.countryCode = Objects.requireNonNull(countryCode, "countryCode cannot be null");
		//for whatever reason if pfpId is null just set it to something random
		this.pfpId = pfpId == null ? new Random().nextInt(ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("pfpIdMax", Integer::parseInt) + 1) : pfpId;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ElephantUser elephantUser) {
			return this.id.equals(elephantUser.id);
		}

		return false;
	}
}
