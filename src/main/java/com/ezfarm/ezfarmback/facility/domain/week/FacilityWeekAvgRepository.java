package com.ezfarm.ezfarmback.facility.domain.week;

import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityWeekAvgRepository extends JpaRepository<FacilityWeekAvg, Long>,
    FacilityWeekRepositoryCustom {

}
