package me.elephantsuite.user.config;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserConfigRepositoryService {

	private final UserConfigRepository repository;

	public void save(UserConfig config) {
		repository.save(config);
	}
}
