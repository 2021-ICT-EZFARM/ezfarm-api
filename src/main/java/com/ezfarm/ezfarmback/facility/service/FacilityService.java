package com.ezfarm.ezfarmback.facility.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FacilityService {

    private final FarmRepository farmRepository;

    private final FacilityDayAvgRepository facilityDayAvgRepository;

    private final FacilityMonthAvgRepository facilityMonthAvgRepository;

    private final FacilityWeekAvgRepository facilityWeekAvgRepository;

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

        return FacilityResponse.listOfDayAvg(DayAvgs);
    }

    public List<FacilityResponse> findFacilityWeekAvg(Long farmId,
        FacilityWeekAvgRequest facilityWeekAvgRequest) {
        Farm findFarm = confirmExistingFarm(farmId);

        List<FacilityWeekAvg> weekAvgs = facilityWeekAvgRepository.findAllByFarmAndMeasureDateStartsWith(
            findFarm, facilityWeekAvgRequest);
        return FacilityResponse.listOfWeekAvg(weekAvgs);
    }

    public List<FacilityResponse> findFacilityMonthlyAvg(Long farmId,
        FacilityMonthAvgRequest facilityYearAvgRequest) {
        Farm findFarm = confirmExistingFarm(farmId);

        List<FacilityMonthAvg> monthAvgs = facilityMonthAvgRepository.findAllByFarmAndMeasureDateStartsWith(
            findFarm, facilityYearAvgRequest.getYear());

        return FacilityResponse.listOfMonthAvg(monthAvgs);
    }

    private Farm confirmExistingFarm(Long farmId) {
        return farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID));
    }
}
