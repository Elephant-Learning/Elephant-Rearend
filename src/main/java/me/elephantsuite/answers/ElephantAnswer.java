package me.elephantsuite.answers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.answers.comment.Comment;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class ElephantAnswer {



	@Id
	@SequenceGenerator(name = "elephant_answer_sequence", sequenceName = "elephant_answer_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elephant_answer_sequence")
	private Long id;

	private String title;

	private String description;

	private boolean answered = false;

	private int numberOfLikes = 0;

	@OneToMany(mappedBy = "answer",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Comment> comments = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Integer> tags = new ArrayList<>();

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonBackReference
	private ElephantUser user;

	private LocalDateTime lastUpdated = LocalDateTime.now();

	private LocalDateTime created = LocalDateTime.now();

	public ElephantAnswer(String title, String description, ElephantUser user) {
		this.title = title;
		this.description = description;
		this.user = user;
	}

	public void incrementLikes() {
		numberOfLikes++;
	}

	public void updateLastUpdatedTime() {
		LocalDateTime.now();
	}

	public void decrementLikes() {
		numberOfLikes--;
	}
}
