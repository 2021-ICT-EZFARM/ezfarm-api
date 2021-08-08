package com.ezfarm.ezfarmback.facility.domain.month;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityMonthAvgRepository extends JpaRepository<FacilityMonthAvg, Long> {

    List<FacilityMonthAvg> findAllByFarmAndMeasureDateStartsWith(Farm farm, String measureDate);

}
