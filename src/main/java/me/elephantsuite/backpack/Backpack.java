package me.elephantsuite.backpack;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	@JsonBackReference
	private ElephantUser user;

	public Backpack(ElephantUser user) {
		this.user = user;
	}

}
