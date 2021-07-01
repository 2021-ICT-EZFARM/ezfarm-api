package com.ezfarm.ezfarmback.remote.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemoteRepository extends JpaRepository<Remote, Long> {

  Optional<Remote> findByFarm(Long farmId);
}
