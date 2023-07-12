package me.elephantsuite.timeline.marker;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MarkerRepositoryService {

	private final MarkerRepository repository;

	public Marker save(Marker marker) {
		return repository.save(marker);
	}

	public Marker getById(long id) {
		if (repository.existsById(id)) {
			return repository.getReferenceById(id);
		}

		return null;
	}

	public void delete(Marker marker) {
		repository.delete(marker);
	}

	public void deleteAll(List<Marker> markers) {
		repository.deleteAll(markers);
	}
}
