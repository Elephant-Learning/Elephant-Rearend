package me.elephantsuite.user.password;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
	@Transactional
	@Query("SELECT ResetPasswordToken FROM ResetPasswordToken t WHERE t.token = ?1")
	ResetPasswordToken getByToken(String token);
}
