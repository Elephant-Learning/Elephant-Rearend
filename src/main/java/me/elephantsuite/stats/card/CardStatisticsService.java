package me.elephantsuite.stats.card;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CardStatisticsService {

	private final CardStatisticsRepository cardStatisticsRepository;

	public CardStatistics save(CardStatistics cardStatistics) {
		return cardStatisticsRepository.save(cardStatistics);
	}
}
