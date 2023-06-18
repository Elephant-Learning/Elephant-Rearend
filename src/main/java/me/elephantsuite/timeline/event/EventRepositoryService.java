package me.elephantsuite.timeline.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventRepositoryService {

    private final EventRepository repository;

    public Event save(Event event) {
        return repository.save(event);
    }

    public Event getById(long id) {
        if (repository.existsById(id)) {
            return repository.getReferenceById(id);
        }

        return null;
    }

    public void delete(Event event) {
        repository.delete(event);
    }
}
