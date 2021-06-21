package com.ezfarm.ezfarmback.farm.controller;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
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

    @GetMapping
    public ResponseEntity<List<FarmResponse>> allFarm(@CurrentUser User user) {
        List<FarmResponse> farmResponses = farmService.viewAllFarms(user);
        return ResponseEntity.ok(farmResponses);
    }

    @GetMapping("/{farmId}")
    public ResponseEntity<FarmResponse> viewFarm(@CurrentUser User user, @PathVariable Long farmId) {
        FarmResponse farmResponse = farmService.viewFarm(user, farmId);
        return ResponseEntity.ok(farmResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createFarm(@CurrentUser User user,
        @Valid @RequestBody FarmRequest farmRequest) {
        Long farmId = farmService.createFarm(user, farmRequest);
        return ResponseEntity.created(URI.create("/api/farm/" + farmId)).build();
    }

    @PatchMapping("/{farmId}")
    public ResponseEntity<Void> updateFarm(@CurrentUser User user, @PathVariable Long farmId,
        @Valid @RequestBody FarmRequest farmRequest) {
        farmService.updateFarm(user, farmId, farmRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{farmId}")
    public ResponseEntity<Void> deleteFarm(@CurrentUser User user, @PathVariable Long farmId) {
        farmService.deleteFarm(user, farmId);
        return ResponseEntity.noContent().build();
    }
}
