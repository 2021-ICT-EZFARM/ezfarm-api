package com.ezfarm.ezfarmback.facility.controller;

import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.facility.service.FacilityService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<FacilityAvgResponse>> findFacilityDailyAvg(@PathVariable Long farmId,
        @RequestBody FacilityDailyAvgRequest facilityDailyAvgRequest) {
        List<FacilityAvgResponse> res = facilityService.findFacilityDailyAvg(farmId,
            facilityDailyAvgRequest);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "타 농가 상세 조회(주)")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @PostMapping("/week-avg/{farmId}")
    public ResponseEntity<List<FacilityAvgResponse>> findFacilityWeekAvg(@PathVariable Long farmId,
        @RequestBody FacilityWeekAvgRequest facilityWeekAvgRequest) {
        List<FacilityAvgResponse> res = facilityService.findFacilityWeekAvg(farmId,
            facilityWeekAvgRequest);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "타 농가 상세 조회(월)")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @PostMapping("/monthly-avg/{farmId}")
    public ResponseEntity<List<FacilityAvgResponse>> findFacilityMonthlyAvg(
        @PathVariable Long farmId, @RequestBody FacilityMonthAvgRequest facilityYearAvgRequest) {
        List<FacilityAvgResponse> res = facilityService.findFacilityMonthlyAvg(farmId,
            facilityYearAvgRequest);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "내 농가 실시간 센서값 조회")
    @ApiResponses({
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 500, message = "서버에 문제가 생겼습니다."),
        @ApiResponse(code = 403, message = "해당 농가에 권한이 없습니다."),
    })
    @GetMapping("{farmId}")
    public ResponseEntity<FacilityResponse> findLiveFacility(@CurrentUser User user, @PathVariable Long farmId) {
        FacilityResponse facilityResponse = facilityService.findLiveFacility(user, farmId);
        return ResponseEntity.ok(facilityResponse);
    }

    @ApiOperation(value = "메인 농가 최근 센서값 조회")
    @ApiResponses({
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 500, message = "서버에 문제가 생겼습니다."),
    })
    @GetMapping()
    public ResponseEntity<FacilityResponse> findMainFarmFacility(@CurrentUser User user) {
        FacilityResponse facilityResponse = facilityService.findMainFarmFacility(user);

        return ResponseEntity.ok(facilityResponse);
    }
}
