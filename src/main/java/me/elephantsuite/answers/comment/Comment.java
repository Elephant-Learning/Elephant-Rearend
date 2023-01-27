package me.elephantsuite.answers.comment;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.answers.ElephantAnswer;
import me.elephantsuite.answers.reply.Reply;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class Comment {

	@Id
	@SequenceGenerator(name = "comment_sequence", sequenceName = "comment_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_sequence")
	private Long id;

	private Long commenterId;

	private String commenterName;

	private int commenterPfpId;

	private String description;

	private int numberOfLikes = 0;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonBackReference
	private ElephantAnswer answer;

	@OneToMany(mappedBy = "comment",  cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Reply> replies = new ArrayList<>();

	public Comment(String description, ElephantAnswer answer, ElephantUser commenter) {
		this.description = description;
		this.answer = answer;
		this.commenterId = commenter.getId();
		this.commenterName = commenter.getFullName();
		this.commenterPfpId = commenter.getPfpId();
	}

	public void incrementLikes() {
		numberOfLikes++;
	}

	public void decrementLikes() {
		numberOfLikes--;
	}

}
