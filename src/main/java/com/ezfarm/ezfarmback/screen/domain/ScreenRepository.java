package com.ezfarm.ezfarmback.screen.domain;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Optional<Screen> findByFarmAndMeasureTime(Farm farm, String measureTime);
}
