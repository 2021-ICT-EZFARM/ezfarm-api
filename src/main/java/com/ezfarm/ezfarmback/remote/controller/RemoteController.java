package com.ezfarm.ezfarmback.remote.controller;

import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.remote.service.RemoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "제어 API")
@RequiredArgsConstructor
@RequestMapping("/api/farm/{farmId}/remote")
@RestController
public class RemoteController {

  private final RemoteService remoteService;

  @ApiOperation(value = "농장 제어 값 조회")
  @GetMapping
  public ResponseEntity<RemoteResponse> viewAllRemote(@PathVariable Long farmId) {
    RemoteResponse response = remoteService.viewRemote(farmId);
    return ResponseEntity.ok(response);
  }

  @ApiOperation(value = "농장 제어 값 수정")
  @PostMapping
  public ResponseEntity<Void> updateRemote(@PathVariable Long farmId, @Valid @RequestBody
      RemoteRequest remoteRequest) {
    remoteService.updateRemote(farmId, remoteRequest);

    return ResponseEntity.ok().build();
  }
}
