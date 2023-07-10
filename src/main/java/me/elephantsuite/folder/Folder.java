package me.elephantsuite.folder;

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
import me.elephantsuite.deck.Deck;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = {"user"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Folder {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "folder_generator")
	@SequenceGenerator(name = "folder_generator", sequenceName = "folder_sequence", allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonBackReference
	@JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
	private ElephantUser user;

	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> deckIds = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Long> timelineIds = new ArrayList<>();

	public Folder(List<Long> deckIds, ElephantUser user, String name) {
		this.deckIds = deckIds;
		this.user = user;
		this.name = name;
	}

}
