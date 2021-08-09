package com.ezfarm.ezfarmback.facility.domain.week;

import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.util.List;

public interface FacilityWeekRepositoryCustom {

    List<FacilityWeekAvg> findAllByFarmAndMeasureDateStartsWith(Farm farm,
        FacilityWeekAvgRequest facilityWeekAvgRequest);
}
