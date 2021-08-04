package com.ezfarm.ezfarmback.facility.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.day.FacilityDailyAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.month.FacilityMonthlyAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.week.FacilityWeekAvgResponse;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.detail.FarmDetailSearchCond;
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
        Farm findFarm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));

        return facilityDayAvgRepository.findMinAndMaxMeasureDateByFarm(findFarm);
    }

    public FacilityDailyAvgResponse findFacilityDailyAvg(Long farmId,
        FarmDetailSearchCond farmDetailSearchCond) {
        Farm findFarm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));
        return null;
    }

    public FacilityWeekAvgResponse findFacilityWeekAvg(Long farmId,
        FarmDetailSearchCond farmDetailSearchCond) {
        Farm findFarm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));
        return null;
    }

    public FacilityMonthlyAvgResponse findFacilityMonthlyAvg(Long farmId,
        FarmDetailSearchCond farmDetailSearchCond) {
        Farm findFarm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));
        return null;
    }
}
