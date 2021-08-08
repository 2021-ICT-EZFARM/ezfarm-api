package com.ezfarm.ezfarmback.screen.controller;

import com.ezfarm.ezfarmback.screen.dto.ScreenResponse;
import com.ezfarm.ezfarmback.screen.service.ScreenService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses({
        @ApiResponse(code = 403, message = "해당 농가에 권한이 없습니다."),
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 404, message = "화면을 조회할 수 없습니다."),
        @ApiResponse(code = 500, message = "IOT 서버에 문제가 생겼습니다."),
        @ApiResponse(code = 500, message = "IOT 서버 연결에 실패했습니다.")
    })
    @GetMapping("/live")
    public ResponseEntity<ScreenResponse> findLiveScreen(@CurrentUser User user,
        @RequestParam Long farmId) {
        ScreenResponse screenResponse = screenService.findLiveScreen(user, farmId);
        return ResponseEntity.ok(screenResponse);
    }
}
