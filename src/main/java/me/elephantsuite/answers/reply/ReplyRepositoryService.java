package me.elephantsuite.answers.reply;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReplyRepositoryService {

	private final ReplyRepository repository;
}
