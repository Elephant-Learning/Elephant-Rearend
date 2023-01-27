package me.elephantsuite.answers;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantAnswerRepositoryService {

	private final ElephantAnswerRepository repository;

	public ElephantAnswer save(ElephantAnswer answer) {
		return repository.save(answer);
	}

	public ElephantAnswer getAnswerById(long answerId) {
		if (repository.existsById(answerId)) {
			return repository.getReferenceById(answerId);
		}

		return null;
	}

	public void delete(ElephantAnswer answer) {
		repository.delete(answer);
	}


	public List<ElephantAnswer> getAllAnswers() {
		return repository.getAllAnswers();
	}
}
