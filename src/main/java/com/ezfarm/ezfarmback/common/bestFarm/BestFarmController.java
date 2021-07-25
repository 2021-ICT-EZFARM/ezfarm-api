package com.ezfarm.ezfarmback.common.bestFarm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
@RequestMapping("/api/bestFarm")
@RestController
public class BestFarmController {

  private final BestFarmApi bestFarmApi;

  @GetMapping()
  public ResponseEntity<Void> saveAllBestFarm() throws Exception {
    bestFarmApi.saveTomatoVinyl();
    bestFarmApi.saveTomatoGlass();
    bestFarmApi.saveStrawberryVinyl();
    bestFarmApi.savePaprikaVinyl();
    bestFarmApi.savePaprikaGlass();
    return ResponseEntity.ok().build();
  }

  @GetMapping("/tomato")
  public ResponseEntity<Void> saveBestTomatoFarm() throws Exception {
    bestFarmApi.saveTomatoVinyl();
    bestFarmApi.saveTomatoGlass();
    return ResponseEntity.ok().build();
  }

  @GetMapping("/strawberry")
  public ResponseEntity<Void> saveBestStrawberryFarm() throws Exception {
    bestFarmApi.saveStrawberryVinyl();
    return ResponseEntity.ok().build();
  }

  @GetMapping("/paprika")
  public ResponseEntity<Void> saveBestPaprikaFarm() throws Exception {
    bestFarmApi.savePaprikaVinyl();
    bestFarmApi.savePaprikaGlass();
    return ResponseEntity.ok().build();
  }
}
