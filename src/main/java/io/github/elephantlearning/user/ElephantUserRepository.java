package io.github.elephantlearning.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface ElephantUserRepository {

	Optional<ElephantUser> findByEmail(String email);
}
