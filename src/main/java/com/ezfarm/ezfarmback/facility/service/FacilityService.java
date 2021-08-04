package com.ezfarm.ezfarmback.facility.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.detail.FarmDetailSearchCond;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FacilityService {

    private final FarmRepository farmRepository;

    private final FacilityDayAvgRepository facilityDayAvgRepository;

    @Transactional(readOnly = true)
    public FacilityPeriodResponse findFacilitySearchPeriod(Long farmId) {
        Farm findFarm = confirmExistingFarm(farmId);
        return facilityDayAvgRepository.findMinAndMaxMeasureDateByFarm(findFarm);
    }

    public List<FacilityResponse> findFacilityDailyAvg(Long farmId,
        FacilityDailyAvgRequest facilityDailyAvgRequest) {
        Farm findFarm = confirmExistingFarm(farmId);

        String date = facilityDailyAvgRequest.getYear() + "-" + facilityDailyAvgRequest.getMonth();
        List<FacilityDayAvg> DayAvgs = facilityDayAvgRepository.findAllByFarmAndMeasureDateStartsWith(
            findFarm, date);

        return FacilityResponse.listOf(DayAvgs);
    }

    public FacilityResponse findFacilityWeekAvg(Long farmId,
        FarmDetailSearchCond farmDetailSearchCond) {
        Farm findFarm = confirmExistingFarm(farmId);
        return null;
    }

    public FacilityResponse findFacilityMonthlyAvg(Long farmId,
        FarmDetailSearchCond farmDetailSearchCond) {
        Farm findFarm = confirmExistingFarm(farmId);
        return null;
    }


    private Farm confirmExistingFarm(Long farmId) {
        return farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));
    }
}
