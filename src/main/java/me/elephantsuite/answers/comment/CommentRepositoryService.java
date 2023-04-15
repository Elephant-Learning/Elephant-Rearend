package me.elephantsuite.answers.comment;

import lombok.AllArgsConstructor;
import me.elephantsuite.answers.ElephantAnswer;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentRepositoryService {

	private final CommentRepository commentRepository;

	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	public Comment getCommentById(long commentId) {
		if (commentRepository.existsById(commentId)) {
			return commentRepository.getReferenceById(commentId);
		}

		return null;
	}

	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}

	public CommentRepository getRepository() {
		return commentRepository;
	}
}
