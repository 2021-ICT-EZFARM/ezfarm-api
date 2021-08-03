package com.ezfarm.ezfarmback.facility.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthlyAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgResponse;
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

    private final FacilityMonthAvgRepository facilityMonthAvgRepository;

    @Transactional(readOnly = true)
    public FacilityPeriodResponse findFacilitySearchPeriod(Long farmId) {
        Farm findFarm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));

        return facilityMonthAvgRepository.findMinAndMinMeasureDateByFarm(findFarm);
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
