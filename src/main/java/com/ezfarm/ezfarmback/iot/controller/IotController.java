package com.ezfarm.ezfarmback.iot.controller;

import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.iot.dto.ScreenResponse;
import com.ezfarm.ezfarmback.iot.service.IotService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "IOT API")
@RequiredArgsConstructor
@RequestMapping("/api/iot")
@RestController
public class IotController {

  private final IotService iotService;

  @ApiOperation(value = "내 농가 실시간 화면 조회")
  @GetMapping("/live-screen")
  public ResponseEntity<ScreenResponse> findLiveScreen(@CurrentUser User user,
      @RequestParam Long farmId) {
    ScreenResponse screenResponse = iotService.findLiveScreen(user, farmId);
    return ResponseEntity.ok(screenResponse);
  }

  @ApiOperation(value = "농가 제어 값 조회")
  @GetMapping("/remote")
  public ResponseEntity<RemoteResponse> findAllRemote(@RequestParam Long farmId) {
    RemoteResponse response = iotService.findRemote(farmId);
    return ResponseEntity.ok(response);
  }

  @ApiOperation(value = "농가 제어 값 수정")
  @PatchMapping("/remote")
  public ResponseEntity<Void> updateRemote(@CurrentUser User user,
      @Valid @RequestBody RemoteRequest request) {
    iotService.updateRemote(user, request);
    return ResponseEntity.ok().build();
  }
}
