package me.elephantsuite.answers.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentRepositoryService {

	private final CommentRepository commentRepository;
}
