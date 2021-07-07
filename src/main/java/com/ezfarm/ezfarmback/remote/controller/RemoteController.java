package com.ezfarm.ezfarmback.remote.controller;

import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.remote.service.RemoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "농가 제어 API")
@RequiredArgsConstructor
@RequestMapping("/api/remote")
@RestController
public class RemoteController {

    private final RemoteService remoteService;

    @ApiOperation(value = "농가 제어 값 조회")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @GetMapping
    public ResponseEntity<RemoteResponse> findAllRemote(@RequestParam Long farmId) {
        RemoteResponse response = remoteService.findRemote(farmId);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "농가 제어 값 수정")
    @PatchMapping
    public ResponseEntity<Void> updateRemote(@Valid @RequestBody RemoteRequest remoteRequest) {
        remoteService.updateRemote(remoteRequest);
        return ResponseEntity.ok().build();
    }
}
