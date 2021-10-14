package com.ezfarm.ezfarmback.facility.controller;

import com.ezfarm.ezfarmback.facility.dto.FacilityAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.facility.service.FacilityService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "시설 API")
@RequiredArgsConstructor
@RequestMapping("/api/facility")
@RestController
public class FacilityController {

  private final FacilityService facilityService;

  @ApiOperation(value = "검색 가능한 기간 조회")
  @GetMapping("/search-condition/{farmId}")
  public ResponseEntity<FacilityPeriodResponse> findFacilitySearchPeriod(
      @PathVariable Long farmId) {
    FacilityPeriodResponse res = facilityService.findFacilitySearchPeriod(farmId);
    return ResponseEntity.ok(res);
  }

  @ApiOperation(value = "타 농가 상세 조회(일)")
  @GetMapping("/daily-avg/{farmId}")
  public ResponseEntity<List<FacilityAvgResponse>> findFacilityDailyAvg(@PathVariable Long farmId,
      @Valid @ModelAttribute FacilityDailyAvgRequest request) {
    List<FacilityAvgResponse> res = facilityService.findFacilityDailyAvg(farmId, request);
    return ResponseEntity.ok(res);
  }

  @ApiOperation(value = "타 농가 상세 조회(주)")
  @GetMapping("/week-avg/{farmId}")
  public ResponseEntity<List<FacilityAvgResponse>> findFacilityWeekAvg(@PathVariable Long farmId,
      @Valid @ModelAttribute FacilityWeekAvgRequest request) {
    List<FacilityAvgResponse> res = facilityService.findFacilityWeekAvg(farmId, request);
    return ResponseEntity.ok(res);
  }

  @ApiOperation(value = "타 농가 상세 조회(월)")
  @GetMapping("/monthly-avg/{farmId}")
  public ResponseEntity<List<FacilityAvgResponse>> findFacilityMonthlyAvg(
      @PathVariable Long farmId, @Valid @ModelAttribute FacilityMonthAvgRequest request) {
    List<FacilityAvgResponse> res = facilityService.findFacilityMonthlyAvg(farmId, request);
    return ResponseEntity.ok(res);
  }

  @ApiOperation(value = "내 농가 실시간 센서값 조회")
  @GetMapping("/{farmId}")
  public ResponseEntity<FacilityResponse> findLiveFacility(@CurrentUser User user,
      @PathVariable Long farmId) {
    FacilityResponse facilityResponse = facilityService.findLiveFacility(user, farmId);
    return ResponseEntity.ok(facilityResponse);
  }

  @ApiOperation(value = "메인 농가 최근 센서값 조회")
  @GetMapping("/recent")
  public ResponseEntity<FacilityResponse> findMainFarmFacility(@CurrentUser User user) {
    FacilityResponse facilityResponse = facilityService.findMainFarmFacility(user);
    return ResponseEntity.ok(facilityResponse);
  }
}
