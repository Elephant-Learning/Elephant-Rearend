package me.elephantsuite.timeline;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TimelineRepositoryService {

    private final TimelineRepository repository;
}
