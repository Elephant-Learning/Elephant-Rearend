package me.elephantsuite.user.password;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
	@Transactional
	@Query(value = "SELECT * FROM reset_password_token WHERE reset_password_token.token = ?1", nativeQuery = true)
	ResetPasswordToken getByToken(String token);
}
