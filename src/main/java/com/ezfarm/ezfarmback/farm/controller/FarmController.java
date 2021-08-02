package com.ezfarm.ezfarmback.farm.controller;

import com.ezfarm.ezfarmback.common.dto.Pagination;
import com.ezfarm.ezfarmback.farm.dto.detail.FarmDetailSearchCond;
import com.ezfarm.ezfarmback.farm.dto.detail.FarmDetailSearchResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RestController()
public class FarmController {

    private final FarmService farmService;

    @ApiOperation(value = "나의 모든 농가 조회")
    @GetMapping("/me")
    public ResponseEntity<List<FarmResponse>> findMyFarms(@CurrentUser User user) {
        List<FarmResponse> farmResponses = farmService.findMyFarms(user);
        return ResponseEntity.ok(farmResponses);
    }

    @ApiOperation(value = "나의 농가 조회")
    @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    @GetMapping("/me/{farmId}")
    public ResponseEntity<FarmResponse> findFarm(@PathVariable Long farmId) {
        FarmResponse farmResponse = farmService.findMyFarm(farmId);
        return ResponseEntity.ok(farmResponse);
    }

    @ApiOperation(value = "나의 농가 생성")
    @ApiResponse(code = 400, message = "잘못된 농가 생성일을 입력했습니다.")
    @PostMapping("/me")
    public ResponseEntity<Void> createFarm(@CurrentUser User user,
        @Valid @RequestBody FarmRequest farmRequest) {
        Long farmId = farmService.createFarm(user, farmRequest);
        return ResponseEntity.created(URI.create("/api/farm/" + farmId)).build();
    }

    @ApiOperation(value = "나의 농가 수정")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 400, message = "잘못된 농가 생성일을 입력했습니다."),
        @ApiResponse(code = 403, message = "해당 농가에 권한이 없습니다.")
    })
    @PatchMapping("/me/{farmId}")
    public ResponseEntity<Void> updateMyFarm(@CurrentUser User user, @PathVariable Long farmId,
        @Valid @RequestBody FarmRequest farmRequest) {
        farmService.updateMyFarm(user, farmId, farmRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "나의 농가 삭제")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 403, message = "해당 농가에 권한이 없습니다.")
    })
    @DeleteMapping("/me/{farmId}")
    public ResponseEntity<Void> deleteMyFarm(@CurrentUser User user, @PathVariable Long farmId) {
        farmService.deleteMyFarm(user, farmId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "타 농가 조회")
    @PostMapping("/other")
    public ResponseEntity<List<FarmSearchResponse>> findOtherFarms(@CurrentUser User user,
        @RequestBody FarmSearchCond farmSearchCond, Pagination pagination) {
        List<FarmSearchResponse> otherFarms = farmService
            .findOtherFarms(user, farmSearchCond, pagination);
        return ResponseEntity.ok(otherFarms);
    }

    @ApiOperation(value = "타 농가 상세 조회")
    @GetMapping("/other/{farmId}")
    public ResponseEntity<FarmDetailSearchResponse> findOtherFarm(@PathVariable Long farmId,
        @RequestBody FarmDetailSearchCond farmDetailSearchCond) {
        FarmDetailSearchResponse otherFarm = farmService.findOtherFarm(farmId, farmDetailSearchCond);
        return ResponseEntity.ok(otherFarm);
    }
}
