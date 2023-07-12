package me.elephantsuite.stats.medal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.stats.ElephantUserStatistics;
import me.elephantsuite.stats.ElephantUserStatisticsRepositoryService;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MedalService {

	private final ElephantUserService userService;

	private final MedalRepository medalRepository;

	private final ElephantUserStatisticsRepositoryService elephantUserStatisticsRepositoryService;

	public <T> void updateEntityMedals(List<T> decks, ElephantUserStatistics statistics, MedalType type, int[] tiers) {

		Medal medal = getMedal(statistics.getMedals(), type);

		if (medal == null) {
			medal = new Medal(type, statistics);
		} else {
			medal = medal.copy();
			statistics.getMedals().remove(medal);
		}

		if (tiers.length != 4) {
			throw new IllegalArgumentException("Medal Tier System did not have 4 tiers present!");
		}

		if (decks.size() >= tiers[0]) {
			medal.setLevel(1);
		} if (decks.size() >= tiers[1]) {
			medal.setLevel(2);
		} if (decks.size() >= tiers[2]) {
			medal.setLevel(3);
		} if (decks.size() >= tiers[3]) {
			medal.setLevel(4);
		}

		updateEarnedTimes(medal);

		statistics.getMedals().add(medal);

		medalRepository.save(medal);

		elephantUserStatisticsRepositoryService.save(statistics);

		updateMedalMedals(statistics);
	}

	private void updateEarnedTimes(Medal medal) {

		if (medal.getEarnedTimes().size() != 5) {
			medal.setEarnedTimes(new ArrayList<>());
			for (int i = 0; i < 5; i++) {
				medal.getEarnedTimes().add(null);
			}
		}

		for (int i = medal.getLevel(); i >= 0; i--) {
			if (medal.getEarnedTimes().get(i) == null) {
				medal.getEarnedTimes().set(i, LocalDateTime.now());
			}
		}

		if (medal.getEarnedTimes().get(medal.getLevel()) == null) {


			medal.getEarnedTimes().set(medal.getLevel(), LocalDateTime.now());
		}


	}

	// should only be one medal of each type in user stats list
	public Medal getMedal(List<Medal> decks, MedalType type) {
		for (Medal deck : decks) {
			if (deck.getType().equals(type)) {
				return deck;
			}
		}

		return null;
	}

	public void updateLoginMedal(ElephantUserStatistics statistics) {
		Medal medal = getMedal(statistics.getMedals(), MedalType.MASTER_STREAKER);

		if (medal == null) {
			medal = new Medal(MedalType.MASTER_STREAKER, statistics);
		} else {
			medal = medal.copy();
			statistics.getMedals().remove(medal);
		}

		int streak = statistics.getDaysStreak();

		if (streak >= 7) {
			medal.setLevel(1);
		} if (streak >= 14) {
			medal.setLevel(2);
		} if (streak >= 31) {
			medal.setLevel(3);
		} if (streak >= 62) {
			medal.setLevel(4);
		}

		updateEarnedTimes(medal);


		statistics.getMedals().add(medal);

		medalRepository.save(medal);

		elephantUserStatisticsRepositoryService.save(statistics);

		updateMedalMedals(statistics);
	}

	public void updateMedalMedals(ElephantUserStatistics statistics) {
		Medal medal = getMedal(statistics.getMedals(), MedalType.BADGE_MASTER);

		if (medal == null) {
			medal = new Medal(MedalType.BADGE_MASTER, statistics);
		} else {
			medal = medal.copy();
			statistics.getMedals().remove(medal);
		}

		List<Medal> medals = statistics.getMedals();

		if (medals.size() == 5) {
			Integer[] levels = medals
				.stream()
				.map(Medal::getLevel)
				.toArray(Integer[]::new);

			Arrays.sort(levels);

			medal.setLevel(levels[0]);
		}

		updateEarnedTimes(medal);

		statistics.getMedals().add(medal);

		medalRepository.save(medal);

		elephantUserStatisticsRepositoryService.save(statistics);
	}

	public void deleteMedal(long id) {
		medalRepository.deleteById(id);
	}
}
