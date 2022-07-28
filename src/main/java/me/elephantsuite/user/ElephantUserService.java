package me.elephantsuite.user;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
//loads data from user repository
public class ElephantUserService {

	private final ElephantUserRepository elephantUserRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final ConfirmationTokenService confirmationTokenService;

	public ConfirmationToken signUpUser(ElephantUser user) {

		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);

		user.setToken(confirmationToken);

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		elephantUserRepository.save(user);

		return confirmationToken;
	}

	public boolean isUserAlreadyRegistered(ElephantUser user) {
		return elephantUserRepository.findByEmail(user.getEmail()).isPresent();
	}

	public Optional<ElephantUser> getUserByEmail(String email) {
		return elephantUserRepository.findByEmail(email);
	}

	public long getUserId(ElephantUser user) {
		return elephantUserRepository.getId(user.getEmail());
	}

	public ElephantUser getUserById(long id) {
		if (elephantUserRepository.existsById(id)) {
			return elephantUserRepository.getReferenceById(id);
		}

		return null;
	}

	public ElephantUser saveUser(ElephantUser user) {
		return elephantUserRepository.save(user);
	}

	public void deleteUser(ElephantUser user) {
		elephantUserRepository.delete(user);
	}
}
