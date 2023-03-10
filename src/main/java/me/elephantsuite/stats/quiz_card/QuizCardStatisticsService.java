package me.elephantsuite.stats.quiz_card;

import lombok.AllArgsConstructor;
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

	public int deleteCardData(long cardId) {
		quizCardStatisticsRepository.deleteCardStats(cardId);
		return quizCardStatisticsRepository.deleteCardMappings(cardId);
	}

	public int deleteCardStats(long cardId) {
		return quizCardStatisticsRepository.deleteCardStats(cardId);
	}
}