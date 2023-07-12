package me.elephantsuite.stats.medal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedalRepository extends JpaRepository<Medal, Long> {
}
