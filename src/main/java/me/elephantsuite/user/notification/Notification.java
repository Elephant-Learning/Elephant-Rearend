package me.elephantsuite.user.notification;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.user.ElephantUser;
import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString
@NoArgsConstructor
public class Notification {
	@Id
	@SequenceGenerator(name = "notification", sequenceName = "notification_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence")
	private Long id;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private String message;

	private final LocalDateTime time = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.EAGER,  cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	@JsonBackReference
	private ElephantUser recipient;

	private Long senderId;

	private Long deckId;

	private Long answerId;

	private Long commentId;

	public Notification(NotificationType type, String message, ElephantUser recipient, Long senderId, Long deckId, Long answerId, Long commentId) {
		this.type = type;
		this.message = message;
		this.recipient = recipient;
		this.senderId = senderId;
		this.deckId = deckId;
		this.answerId = answerId;
		this.commentId = commentId;
	}



}
