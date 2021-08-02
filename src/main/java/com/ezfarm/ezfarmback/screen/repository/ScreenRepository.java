package com.ezfarm.ezfarmback.screen.repository;

import com.ezfarm.ezfarmback.screen.domain.Screen;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

  Optional<Screen> findByFarmAndMeasureTime(Long farmId, String measureTime);
}
