package com.ezfarm.ezfarmback.iot.domain;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemoteRepository extends JpaRepository<Remote, Long> {

  Optional<Remote> findByFarm(Farm farm);
}
