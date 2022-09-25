package me.elephantsuite.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.backpack.BackpackRepositoryService;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepository;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import me.elephantsuite.stats.ElephantUserStatisticsRepositoryService;
import me.elephantsuite.user.notification.NotificationRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
//loads data from user repository
public class ElephantUserService {

	private final ElephantUserRepository elephantUserRepository;

	private final DeckRepository deckRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final ConfirmationTokenService confirmationTokenService;

	private final NotificationRepository notificationRepository;

	private final BackpackRepositoryService backpackRepositoryService;

	private final ElephantUserStatisticsRepositoryService elephantUserStatisticsRepositoryService;

	public ConfirmationToken signUpUser(ElephantUser user) {

		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("tokenExpiredLimitMinutes", Long::parseLong)), user);

		user.setToken(confirmationToken);

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		backpackRepositoryService.saveBackpack(user.getBackpack());

		elephantUserStatisticsRepositoryService.save(user.getElephantUserStatistics());

		elephantUserRepository.save(user);

		return confirmationToken;
	}

	public boolean isUserAlreadyRegistered(String email) {
		return elephantUserRepository.getId(email) != null;
	}



	public ElephantUser getUserByEmail(String email) {
		return getUserById(elephantUserRepository.getId(email));
	}

	public Long getUserId(String email) {
		return elephantUserRepository.getId(email);
	}

	public ElephantUser getUserById(long id) {
		ElephantUser elephantUser = elephantUserRepository.getById(id);

		if (elephantUser == null) {
			return null;
		}

		List<Deck> userDecks = deckRepository.getDecksByUserId(id);

		if (!elephantUser.getDecks().equals(userDecks)) {
			elephantUser.setDecks(userDecks);
			elephantUser = saveUser(elephantUser);
		}

		return elephantUser;
	}

	public ElephantUser saveUser(ElephantUser user) {
		return elephantUserRepository.save(user);
	}

	public void deleteUser(ElephantUser user) {
		notificationRepository.deleteNotificationSenderId(user.getId());
		deckRepository.deleteUserFromSharedDecks(user.getId());
		elephantUserRepository.deleteUserFromFriends(user.getId());
		elephantUserRepository.delete(user);
	}

	public List<ElephantUser> getAllUsers() {
		return elephantUserRepository.getAllUsers();
	}
}
