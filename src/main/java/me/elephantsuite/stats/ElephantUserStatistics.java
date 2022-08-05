package me.elephantsuite.stats;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
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

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@ToString
@Embeddable
public class ElephantUserStatistics {

	private int daysStreak = 0;

	private double usageTime = 0;

	private LocalDateTime lastLoggedIn = LocalDateTime.now();

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
