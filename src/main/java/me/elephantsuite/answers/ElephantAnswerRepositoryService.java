package me.elephantsuite.answers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantAnswerRepositoryService {

	private final ElephantAnswerRepository repository;

}
