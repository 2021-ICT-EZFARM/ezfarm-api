package com.ezfarm.ezfarmback.farm.controller;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
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

@RequiredArgsConstructor
@RequestMapping("/api/farm")
@RestController()
public class FarmController {

    private final FarmRepository farmRepository;

    private final FarmService farmService;

    @ApiOperation(value = "나의 모든 농가 조회")
    @GetMapping
    public ResponseEntity<List<FarmResponse>> allFarm(@CurrentUser User user) {
        List<FarmResponse> farmResponses = farmService.viewAllFarms(user);
        return ResponseEntity.ok(farmResponses);
    }

    @ApiOperation(value = "나의 농가 조회")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 403, message = "콘텐츠에 접근 권한이 없습니다.")
    })
    @GetMapping("/{farmId}")
    public ResponseEntity<FarmResponse> viewFarm(@CurrentUser User user, @PathVariable Long farmId) {
        FarmResponse farmResponse = farmService.viewFarm(user, farmId);
        return ResponseEntity.ok(farmResponse);
    }

    @ApiOperation(value = "나의 농가 생성")
    @ApiResponse(code = 400, message = "잘못된 농가 생성일을 입력했습니다.")
    @PostMapping
    public ResponseEntity<Void> createFarm(@CurrentUser User user,
        @Valid @RequestBody FarmRequest farmRequest) {
        Long farmId = farmService.createFarm(user, farmRequest);
        return ResponseEntity.created(URI.create("/api/farm/" + farmId)).build();
    }

    @ApiOperation(value = "나의 농가 수정")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 403, message = "콘텐츠에 접근 권한이 없습니다."),
        @ApiResponse(code = 400, message = "잘못된 농가 생성일을 입력했습니다.")
    })
    @PatchMapping("/{farmId}")
    public ResponseEntity<Void> updateFarm(@CurrentUser User user, @PathVariable Long farmId,
        @Valid @RequestBody FarmRequest farmRequest) {
        farmService.updateFarm(user, farmId, farmRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "나의 농가 삭제")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다."),
        @ApiResponse(code = 403, message = "콘텐츠에 접근 권한이 없습니다."),
    })
    @DeleteMapping("/{farmId}")
    public ResponseEntity<Void> deleteFarm(@CurrentUser User user, @PathVariable Long farmId) {
        farmService.deleteFarm(user, farmId);
        return ResponseEntity.noContent().build();
    }
}
