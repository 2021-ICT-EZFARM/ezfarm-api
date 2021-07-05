package com.ezfarm.ezfarmback.alert.controller;

import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.alert.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/alert-range")
    public ResponseEntity<AlertRangeResponse> findAlertRange(@RequestParam Long farmId) {
        AlertRangeResponse alertRange = alertService.findAlertRange(farmId);
        return ResponseEntity.ok(alertRange);
    }

    @PatchMapping("/alert-range")
    public ResponseEntity<Void> updateAlertRange(@RequestParam Long alertRangeId,
        @RequestBody AlertRangeRequest alertRangeRequest) {
        alertService.updateAlertRange(alertRangeId, alertRangeRequest);
        return ResponseEntity.ok().build();
    }

}
