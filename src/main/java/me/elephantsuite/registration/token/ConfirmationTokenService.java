package me.elephantsuite.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

	private final ConfirmationTokenRepository confirmationTokenRepository;

	public void saveConfirmationToken(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}

	public Optional<ConfirmationToken> getToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}

	public int setConfirmedAt(String token) {
		return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
	}

	public @Nullable ConfirmationToken getTokenByUser(long userId) {
		return confirmationTokenRepository.getReferenceById(userId);
	}

	public void deleteToken(ConfirmationToken token) {
		confirmationTokenRepository.deleteById(getTokenId(token));
	}

	public long getTokenId(ConfirmationToken token) {
		return confirmationTokenRepository.getId(token.getToken());
	}
}
