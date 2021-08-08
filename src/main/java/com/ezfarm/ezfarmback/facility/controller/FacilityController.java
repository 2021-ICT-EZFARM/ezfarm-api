package com.ezfarm.ezfarmback.facility.controller;

import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.service.FacilityService;
import com.ezfarm.ezfarmback.farm.dto.detail.FarmDetailSearchCond;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "시설 API")
@RequiredArgsConstructor
@RequestMapping("/api/facility")
@RestController
public class FacilityController {

    private final FacilityService facilityService;

    @ApiOperation(value = "검색 가능한 기간 조회")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @GetMapping("/search-condition/{farmId}")
    public ResponseEntity<FacilityPeriodResponse> findFacilitySearchPeriod(
        @PathVariable Long farmId) {
        FacilityPeriodResponse res = facilityService.findFacilitySearchPeriod(farmId);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "타 농가 상세 조회(일)")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @PostMapping("/daily-avg/{farmId}")
    public ResponseEntity<List<FacilityResponse>> findFacilityDailyAvg(@PathVariable Long farmId,
        @RequestBody FacilityDailyAvgRequest facilityDailyAvgRequest) {
        List<FacilityResponse> res = facilityService.findFacilityDailyAvg(farmId,
            facilityDailyAvgRequest);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "타 농가 상세 조회(주)")
    @PostMapping("/week-avg/{farmId}")
    public ResponseEntity<FacilityResponse> findFacilityWeekAvg(@PathVariable Long farmId,
        @RequestBody FarmDetailSearchCond farmDetailSearchCond) {
        FacilityResponse res = facilityService.findFacilityWeekAvg(farmId,
            farmDetailSearchCond);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "타 농가 상세 조회(월)")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @PostMapping("/monthly-avg/{farmId}")
    public ResponseEntity<List<FacilityResponse>> findFacilityMonthlyAvg(
        @PathVariable Long farmId, @RequestBody FacilityMonthAvgRequest facilityYearAvgRequest) {
        List<FacilityResponse> res = facilityService.findFacilityMonthlyAvg(farmId,
            facilityYearAvgRequest);
        return ResponseEntity.ok(res);
    }
}
