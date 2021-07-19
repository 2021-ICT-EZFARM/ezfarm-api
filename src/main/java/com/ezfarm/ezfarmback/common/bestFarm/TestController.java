package com.ezfarm.ezfarmback.common.bestFarm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/test")
@RestController
public class TestController {

  private final BestFarmApi bestFarmApi;

  @GetMapping
  public ResponseEntity<String> viewBestFarm() throws Exception {
    bestFarmApi.saveTomatoVinyl();
    return ResponseEntity.ok().build();
  }
}
