package me.elephantsuite.user;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import me.elephantsuite.registration.token.ConfirmationToken;
import me.elephantsuite.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
//loads data from user repository
public record ElephantUserService(ElephantUserRepository elephantUserRepository,
								  BCryptPasswordEncoder bCryptPasswordEncoder,
								  ConfirmationTokenService confirmationTokenService) implements UserDetailsService {


	/**
	 * Locates the user based on the username. In the actual implementation, the search
	 * may possibly be case sensitive, or case insensitive depending on how the
	 * implementation instance is configured. In this case, the <code>UserDetails</code>
	 * object that comes back may have a username that is of a different case than what
	 * was actually requested..
	 *
	 * @param username the username identifying the user whose data is required.
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws UsernameNotFoundException if the user could not be found or the user has no
	 *                                   GrantedAuthority
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.elephantUserRepository
			.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("Could not find user with email " + username + " !"));
	}

	public ConfirmationToken signUpUser(ElephantUser user) {

		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);

		elephantUserRepository.save(user);

		String token = UUID.randomUUID().toString();

		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);

		confirmationTokenService.saveConfirmationToken(confirmationToken);

		return confirmationToken;
	}

	public boolean isUserAlreadyRegistered(ElephantUser user) {
		return elephantUserRepository.findByEmail(user.getEmail()).isPresent();
	}

	public Optional<ElephantUser> getUserByEmail(String email) {
		return elephantUserRepository.findByEmail(email);
	}

	public int enableAppUser(String email) {
		return elephantUserRepository.enableAppUser(email);
	}

	public long getUserId(ElephantUser user) {
		return elephantUserRepository.getId(user.getEmail());
	}

	public @Nullable
	ElephantUser getUserById(long id) {
		return elephantUserRepository.findByEmail(elephantUserRepository.getEmailById(id)).orElse(null);
	}
}
