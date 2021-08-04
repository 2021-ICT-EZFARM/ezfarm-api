package com.ezfarm.ezfarmback.facility.domain.day;

import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityDayAvgRepository extends JpaRepository<FacilityDayAvg, Long> {

    @Query(value = "select new com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse(min(f.measureDate), max(f.measureDate)) from FacilityDayAvg as f where f.farm = :farm")
    FacilityPeriodResponse findMinAndMaxMeasureDateByFarm(@Param("farm") Farm farm);
}
