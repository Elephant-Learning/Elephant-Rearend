package me.elephantsuite.user.notification;

import java.time.LocalDateTime;

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
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.user.ElephantUser;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Notification {
	@Id
	@SequenceGenerator(name = "notification", sequenceName = "notification_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence")
	private final Long id;

	@Enumerated(EnumType.STRING)
	private final NotificationType type;

	private final String message;

	private final LocalDateTime time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	private ElephantUser recipient;

	private ElephantUser sender;

}
