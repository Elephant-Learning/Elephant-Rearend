package me.elephantsuite.user.password;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ResetPasswordTokenService {

	private final ResetPasswordTokenRepository repository;

	public ResetPasswordToken save(ResetPasswordToken token) {
		return repository.save(token);
	}

	public ResetPasswordToken getByToken(String token) {
		return repository.getByToken(token);
	}

	public void delete(ResetPasswordToken resetToken) {
		repository.delete(resetToken);
	}
}
