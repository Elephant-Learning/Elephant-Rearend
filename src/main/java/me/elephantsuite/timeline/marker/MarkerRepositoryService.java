package me.elephantsuite.timeline.marker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarkerRepositoryService {

	private final MarkerRepository repository;
}
