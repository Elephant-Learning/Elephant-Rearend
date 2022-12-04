package me.elephantsuite.answers.reply;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.answers.ElephantAnswer;
import me.elephantsuite.answers.comment.Comment;
import me.elephantsuite.user.ElephantUser;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class Reply {

	@Id
	@SequenceGenerator(name = "reply_sequence", sequenceName = "reply_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_sequence")
	private Long id;

	private String authorName;

	private int authorPfpId;

	private String text;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JsonBackReference
	private Comment comment;

	public Reply(String text, Comment comment, ElephantUser replyer) {
		this.authorName = replyer.getFullName();
		this.authorPfpId = replyer.getPfpId();
		this.text = text;
		this.comment = comment;
	}
}
