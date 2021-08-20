package com.ezfarm.ezfarmback.facility.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.utils.iot.IotUtils;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.domain.hour.FacilityRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

  private final IotUtils iotUtils;

  private final FacilityRepository facilityRepository;

  public FacilityPeriodResponse findFacilitySearchPeriod(Long farmId) {
    Farm findFarm = confirmExistingFarm(farmId);
    return facilityDayAvgRepository.findMinAndMaxMeasureDateByFarm(findFarm);
  }

  public List<FacilityAvgResponse> findFacilityDailyAvg(Long farmId,
      FacilityDailyAvgRequest facilityDailyAvgRequest) {
    Farm findFarm = confirmExistingFarm(farmId);

    String date = facilityDailyAvgRequest.getYear() + "-" + facilityDailyAvgRequest.getMonth();
    List<FacilityDayAvg> DayAvgs = facilityDayAvgRepository.findAllByFarmAndMeasureDateStartsWith(
        findFarm, date);

    return FacilityAvgResponse.listOfDayAvg(DayAvgs);
  }

  public List<FacilityAvgResponse> findFacilityWeekAvg(Long farmId,
      FacilityWeekAvgRequest facilityWeekAvgRequest) {
    Farm findFarm = confirmExistingFarm(farmId);

    List<FacilityWeekAvg> weekAvgs = facilityWeekAvgRepository
        .findAllByFarmAndMeasureDateStartsWith(
            findFarm, facilityWeekAvgRequest);
    return FacilityAvgResponse.listOfWeekAvg(weekAvgs);
  }

  public List<FacilityAvgResponse> findFacilityMonthlyAvg(Long farmId,
      FacilityMonthAvgRequest facilityYearAvgRequest) {
    Farm findFarm = confirmExistingFarm(farmId);

    List<FacilityMonthAvg> monthAvgs = facilityMonthAvgRepository
        .findAllByFarmAndMeasureDateStartsWith(
            findFarm, facilityYearAvgRequest.getYear());

    return FacilityAvgResponse.listOfMonthAvg(monthAvgs);
  }

  private Farm confirmExistingFarm(Long farmId) {
    return farmRepository.findById(farmId).orElseThrow(
        () -> new CustomException(ErrorCode.INVALID_FARM_ID));
  }

  public FacilityResponse findLiveFacility(User user, Long farmId) {
    Farm farm = confirmExistingFarm(farmId);

    if (!farm.isMyFarm(user.getId())) {
      throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
    }

    String output = iotUtils.getLiveSensorValue(farmId);
    FacilityResponse facilityResponse = FacilityResponse.stringParseToFacilityRes(output);

    return facilityResponse;
  }

  public FacilityResponse findMainFarmFacility(User user) {
    Farm mainFarm = farmRepository.findByIsMainAndUser(true, user)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

    Facility facility = facilityRepository.findTop1ByFarmOrderByMeasureDateDesc(mainFarm)
        .orElse(null);

    if (facility == null) {
      if (facilityRepository.existsByFarm(mainFarm)) {
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
      }
      return new FacilityResponse();
    }

    return FacilityResponse.of(facility);
  }
}
