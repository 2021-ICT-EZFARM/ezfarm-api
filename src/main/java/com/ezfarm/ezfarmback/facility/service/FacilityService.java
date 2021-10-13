package com.ezfarm.ezfarmback.facility.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.iot.IotConnector;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.domain.hour.FacilityRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.User;
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

  private final IotConnector iotConnector;

  private final FacilityRepository facilityRepository;

  public FacilityPeriodResponse findFacilitySearchPeriod(Long farmId) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    return facilityDayAvgRepository.findMinAndMaxMeasureDateByFarm(farm);
  }

  public List<FacilityAvgResponse> findFacilityDailyAvg(Long farmId, FacilityDailyAvgRequest req) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    List<FacilityDayAvg> avg = facilityDayAvgRepository.findAllByFarmAndMeasureDateStartsWith(
        farm, req.getYear() + "-" + req.getMonth());
    return FacilityAvgResponse.listOfDayAvg(avg);
  }

  public List<FacilityAvgResponse> findFacilityWeekAvg(Long farmId, FacilityWeekAvgRequest req) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    List<FacilityWeekAvg> avg = facilityWeekAvgRepository.findAllByFarmAndMeasureDateStartsWith(
        farm, req);
    return FacilityAvgResponse.listOfWeekAvg(avg);
  }

  public List<FacilityAvgResponse> findFacilityMonthlyAvg(Long farmId,
      FacilityMonthAvgRequest req) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    List<FacilityMonthAvg> avg = facilityMonthAvgRepository.findAllByFarmAndMeasureDateStartsWith(
        farm, req.getYear());
    return FacilityAvgResponse.listOfMonthAvg(avg);
  }

  private Farm validateFarmIdAndGetFarm(Long farmId) {
    return farmRepository.findById(farmId).orElseThrow(
        () -> new CustomException(ErrorCode.INVALID_FARM_ID));
  }

  public FacilityResponse findLiveFacility(User user, Long farmId) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    farm.validateIsMyFarm(user);
    return FacilityResponse.stringParseToFacilityRes(iotConnector.getLiveSensorValue(farmId));
  }

  public FacilityResponse findMainFarmFacility(User user) {
    Farm farm = farmRepository.findByUserAndIsMain(user, true)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_MAIN_FARM));
    Facility facility = facilityRepository.findTop1ByFarmOrderByMeasureDateDesc(farm)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_FACILITY_DATA));
    return FacilityResponse.of(facility);
  }
}
