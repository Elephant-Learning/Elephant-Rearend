package me.elephantsuite.registration.token;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
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

	public int addExpiredLimit(ConfirmationToken token, int minAfter) {
		return confirmationTokenRepository.updateExpiredAt(token.getToken(), token.getExpiresAt().plusMinutes(minAfter));
	}

	public ConfirmationToken getTokenByUser(long userId) {
		try {
			return confirmationTokenRepository.getReferenceById(userId);
		} catch (Exception e) {
			return null;
		}
	}

	public void deleteToken(ConfirmationToken token) {
		confirmationTokenRepository.deleteById(getTokenId(token));
	}

	public long getTokenId(ConfirmationToken token) {
		return confirmationTokenRepository.getId(token.getToken());
	}


}
