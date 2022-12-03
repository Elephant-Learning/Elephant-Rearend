package me.elephantsuite.timeline.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventRepositoryService {

    private final EventRepository repository;
}
