package me.elephantsuite.stats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ElephantUserStatisticsRepositoryService {

	private final ElephantUserStatisticsRepository elephantUserStatisticsRepository;

	public ElephantUserStatistics getById(long id) {
		if (elephantUserStatisticsRepository.existsById(id)) {
			return elephantUserStatisticsRepository.getReferenceById(id);
		}

		return null;
	}

	public ElephantUserStatistics save(ElephantUserStatistics statistics) {
		return this.elephantUserStatisticsRepository.save(statistics);
	}
}
