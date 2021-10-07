package com.ezfarm.ezfarmback.screen.controller;

import com.ezfarm.ezfarmback.screen.dto.ScreenResponse;
import com.ezfarm.ezfarmback.screen.service.ScreenService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "내 농가 실시간 화면 API")
@RequiredArgsConstructor
@RequestMapping("/api/screen")
@RestController
public class ScreenController {

  private final ScreenService screenService;

  @ApiOperation(value = "내 농가 실시간 화면 조회")
  @GetMapping("/live")
  public ResponseEntity<ScreenResponse> findLiveScreen(@CurrentUser User user,
      @RequestParam Long farmId) {
    ScreenResponse screenResponse = screenService.findLiveScreen(user, farmId);
    return ResponseEntity.ok(screenResponse);
  }

  @ApiOperation(value = "내 농가 오늘 저장된 모든 화면 조회")
  @GetMapping()
  public ResponseEntity<List<ScreenResponse>> findTodayScreens(@CurrentUser User user,
      @RequestParam Long farmId) {
    List<ScreenResponse> screenResponses = screenService.findTodayScreens(user, farmId);
    return ResponseEntity.ok(screenResponses);
  }
}
