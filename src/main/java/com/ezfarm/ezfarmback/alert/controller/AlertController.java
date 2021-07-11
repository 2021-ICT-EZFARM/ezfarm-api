package com.ezfarm.ezfarmback.alert.controller;

import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.alert.service.AlertService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림 API")
@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @ApiOperation(value = "알림 범위 조회")
    @ApiResponses({
        @ApiResponse(code = 403, message = "해당 농가에 권한이 없습니다."),
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    })
    @GetMapping("/alert-range")
    public ResponseEntity<AlertRangeResponse> findAlertRange(@CurrentUser User user,
        @RequestParam Long farmId) {
        AlertRangeResponse alertRange = alertService.findAlertRange(user, farmId);
        return ResponseEntity.ok(alertRange);
    }

    @ApiOperation(value = "알림 범위 수정")
    @PatchMapping("/alert-range")
    public ResponseEntity<Void> updateAlertRange(@CurrentUser User user,
        @RequestParam Long alertRangeId,
        @RequestBody AlertRangeRequest alertRangeRequest) {
        alertService.updateAlertRange(user, alertRangeId, alertRangeRequest);
        return ResponseEntity.ok().build();
    }

}
