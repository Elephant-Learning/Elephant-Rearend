package me.elephantsuite.backpack;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Backpack {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "backpack_generator")
	@SequenceGenerator(name = "backpack_generator", sequenceName = "backpack_sequence", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Card> cards = new ArrayList<>();

	@OneToOne(mappedBy = "backpack", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonBackReference
	private ElephantUser user;

	public Backpack(ElephantUser user) {
		this.user = user;
	}

}
