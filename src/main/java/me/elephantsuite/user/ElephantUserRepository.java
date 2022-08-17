package me.elephantsuite.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//find users by email
@Transactional(readOnly = true)
@Repository
public interface ElephantUserRepository extends JpaRepository<ElephantUser, Long> {

	// get id of user from database. There is prob an easier way to do this without SQL but do this for now
	@Query("SELECT id FROM ElephantUser e WHERE e.email = ?1")
	Long getId(String email);

	@Query(value = "SELECT * FROM elephant_user", nativeQuery = true)
	List<ElephantUser> getAllUsers();

	@Query(value = "SELECT * FROM elephant_user WHERE elephant_user.id = ?1", nativeQuery = true)
	ElephantUser getById(long id);

}
