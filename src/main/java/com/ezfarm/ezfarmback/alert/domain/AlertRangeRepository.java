package com.ezfarm.ezfarmback.alert.domain;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRangeRepository extends JpaRepository<AlertRange, Long> {

  Optional<AlertRange> findByFarm(Farm farm);
}
