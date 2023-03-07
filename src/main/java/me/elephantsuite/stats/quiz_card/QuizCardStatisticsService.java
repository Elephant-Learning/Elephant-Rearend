package me.elephantsuite.stats.quiz_card;

import lombok.AllArgsConstructor;
import me.elephantsuite.stats.card.CardStatistics;
import me.elephantsuite.stats.card.CardStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class QuizCardStatisticsService {

	private final QuizCardStatisticsRepository quizCardStatisticsRepository;

	public QuizCardStatistics save(QuizCardStatistics cardStatistics) {
		return quizCardStatisticsRepository.save(cardStatistics);
	}
}