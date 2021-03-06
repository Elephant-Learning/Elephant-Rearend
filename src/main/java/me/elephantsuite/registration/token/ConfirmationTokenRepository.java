package me.elephantsuite.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

	Optional<ConfirmationToken> findByToken(String token);

	@Transactional
	@Modifying
	@Query("UPDATE ConfirmationToken c SET c.expiresAt = ?2 WHERE c.token = ?1")
	int updateExpiredAt(String token, LocalDateTime expiredAt);

	@Query("SELECT id FROM ConfirmationToken c WHERE c.token = ?1")
	long getId(String token);
}
