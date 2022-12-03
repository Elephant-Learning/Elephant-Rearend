package me.elephantsuite.timeline;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import org.springframework.stereotype.Service;

import java.sql.Time;

@AllArgsConstructor
@Service
public class TimelineRepositoryService {

    private final TimelineRepository repository;

    public Timeline save(Timeline timeline) {
        return repository.save(timeline);
    }

    public Timeline getTimelineById(long id) {
        if (repository.existsById(id)) {
            return repository.getReferenceById(id);
        }

        return null;
    }

    public void deleteTimeline(Timeline tl) {
        repository.delete(tl);
    }
}
