package com.ezfarm.ezfarmback.farm.controller;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmForm;
import com.ezfarm.ezfarmback.farm.dto.validator.FarmFormValidator;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 내 농가 API
 */
@RequiredArgsConstructor
@RequestMapping("/api/farm")
@RestController()
public class FarmController {

    private final FarmRepository farmRepository;
    private final FarmService farmService;
    private final FarmFormValidator farmValidator;

    @InitBinder("farmForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(farmValidator);
    }

    @GetMapping()
    public List<Farm> allFarm(@CurrentUser User user) {
        List<Farm> farms = farmService.viewAllFarms(user);
        return farms;
    }

    @GetMapping("/{farmId}")
    public Farm viewFarm(@CurrentUser User user, @PathVariable Long farmId) {
        Farm farm = farmService.viewFarm(user, farmId);
        return farm;
    }

    @PostMapping
    public Farm createFarm(@CurrentUser User user, @Valid @RequestBody FarmForm farmForm) {
        Farm farm = farmService.createFarm(user, farmForm);
        return farm;
    }

    @PatchMapping("/{farmId}")
    public Farm updateFarm(@CurrentUser User user, @PathVariable Long farmId, @Valid @RequestBody FarmForm farmForm) {
        Farm updateFarm = farmService.updateFarm(user, farmId, farmForm);
        return updateFarm;
    }

    @DeleteMapping("/{farmId}")
    public List<Farm> deleteFarm(@CurrentUser User user, @PathVariable Long farmId) {
        farmService.deleteFarm(user, farmId);
        return farmRepository.findAll();
    }
}
