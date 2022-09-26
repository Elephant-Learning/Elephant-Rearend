package me.elephantsuite.user.password;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.elephantsuite.user.ElephantUser;

@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResetPasswordToken {

	@Id
	@SequenceGenerator(name = "reset_password_token_sequence", sequenceName = "reset_password_token_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_password_token_sequence")
	private Long id;

	@Column(nullable = false, unique = true)
	private String token;

	private LocalDateTime expiresAt;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "resetPasswordToken")
	@JoinColumn(nullable = false, name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	@JsonBackReference
	private ElephantUser elephantUser;

	public ResetPasswordToken(String token, LocalDateTime expiresAt, ElephantUser elephantUser) {
		this.token = token;
		this.expiresAt = expiresAt;
		this.elephantUser = elephantUser;
	}
}
