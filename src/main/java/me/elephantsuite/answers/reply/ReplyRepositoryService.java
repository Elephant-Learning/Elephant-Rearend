package me.elephantsuite.answers.reply;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReplyRepositoryService {

	private final ReplyRepository repository;

	public Reply save(Reply reply) {
		return repository.save(reply);
	}

	public Reply getReplyById(long replyId) {
		if (repository.existsById(replyId)) {
			return repository.getReferenceById(replyId);
		}

		return null;
	}

	public void delete(Reply reply) {
		repository.delete(reply);
	}
}
