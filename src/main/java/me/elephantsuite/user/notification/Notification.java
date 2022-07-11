package me.elephantsuite.user.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.elephantsuite.user.ElephantUser;

@Entity
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public record Notification(
	@Id @SequenceGenerator(name = "notification", sequenceName = "notification_sequence", allocationSize = 1) @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence") Long id,
	@Enumerated(EnumType.STRING) NotificationType type, String message,
	@ManyToOne ElephantUser recipient, ElephantUser sender) {

}
