package com.ezfarm.ezfarmback.farm.controller;

import com.ezfarm.ezfarmback.common.Pagination;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "농가 API")
@RequiredArgsConstructor
@RequestMapping("/api/farm")
@RestController
public class FarmController {

  private final FarmService farmService;

  @ApiOperation(value = "나의 모든 농가 조회")
  @GetMapping("/me")
  public ResponseEntity<List<FarmResponse>> findMyFarms(@CurrentUser User user) {
    return ResponseEntity.ok(farmService.findMyFarms(user));
  }

  @ApiOperation(value = "나의 농가 조회")
  @GetMapping("/me/{farmId}")
  public ResponseEntity<FarmResponse> findFarm(@PathVariable Long farmId) {
    return ResponseEntity.ok(farmService.findMyFarm(farmId));
  }

  @ApiOperation(value = "나의 농가 생성")
  @PostMapping("/me")
  public ResponseEntity<Void> createFarm(@CurrentUser User user,
      @Valid @RequestBody FarmRequest request) {
    Long farmId = farmService.createFarm(user, request);
    return ResponseEntity.created(URI.create("/api/farm/" + farmId)).build();
  }

  @ApiOperation(value = "나의 농가 수정")
  @PatchMapping("/me/{farmId}")
  public ResponseEntity<Void> updateMyFarm(@CurrentUser User user, @PathVariable Long farmId,
      @Valid @RequestBody FarmRequest request) {
    farmService.updateMyFarm(user, farmId, request);
    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "나의 농가 삭제")
  @DeleteMapping("/me/{farmId}")
  public ResponseEntity<Void> deleteMyFarm(@CurrentUser User user, @PathVariable Long farmId) {
    farmService.deleteMyFarm(user, farmId);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(value = "타 농가 조회")
  @GetMapping("/other")
  public ResponseEntity<List<FarmSearchResponse>> findOtherFarms(@CurrentUser User user,
      FarmSearchCond farmSearchCond, Pagination pagination) {
    return ResponseEntity.ok(farmService.findOtherFarms(user, farmSearchCond, pagination));
  }
}
