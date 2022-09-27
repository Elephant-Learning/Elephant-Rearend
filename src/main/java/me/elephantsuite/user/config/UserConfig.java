package me.elephantsuite.user.config;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.user.ElephantUser;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserConfig {

	@Id
	@SequenceGenerator(name = "user_config", sequenceName = "user_config_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_config_sequence")
	private Long id;

	private boolean disableFriendRequests = false;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "config")
	@JsonBackReference
	private ElephantUser user;

	public UserConfig(ElephantUser user) {
		this.user = user;
	}
}
