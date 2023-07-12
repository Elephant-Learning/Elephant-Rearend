package me.elephantsuite.stats.medal;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.stats.ElephantUserStatistics;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@ToString
@Entity
public class Medal {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medal_generator")
	@SequenceGenerator(name = "medal_generator", sequenceName = "medal_sequence", allocationSize = 1)
	private Long id;

	@Enumerated(EnumType.STRING)
	private MedalType type;

	private int level;

	private LocalDateTime earned;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JsonBackReference
	private ElephantUserStatistics userStatistics;

	public Medal(MedalType medalType, ElephantUserStatistics userStatistics) {
		this.type = medalType;
		this.userStatistics = userStatistics;
		this.level = 0;
	}

	public Medal copy() {
		Medal medal = new Medal(type, userStatistics);
		medal.level = this.level;
		return medal;
	}
}
