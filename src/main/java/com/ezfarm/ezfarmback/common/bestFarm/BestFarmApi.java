package com.ezfarm.ezfarmback.common.bestFarm;

import com.ezfarm.ezfarmback.facility.domain.Facility;
import com.ezfarm.ezfarmback.facility.dto.FacilityRequestDto;
import com.ezfarm.ezfarmback.facility.repository.FacilityRepository;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Component
public class BestFarmApi {

  private final ModelMapper modelMapper;
  private final FarmRepository farmRepository;
  private final FacilityRepository facilityRepository;
  private String serviceKey = "kvkoj/tBF7qWoxvAhz7HsCIG7pgmSGhLhq9W0H84wn3cPDAUwblBLBI6drAyqwaFXVXtn1nJtls9aySwrho1CQ==";

  public void saveTomatoVinyl() throws Exception {
    Resource resource = new ClassPathResource("bestFarm/tomato_vinyl.csv");
    saveBestFarms(resource, CropType.TOMATO, FarmType.VINYL);
  }

  private void saveBestFarms(Resource resource, CropType cropType, FarmType farmType)
      throws Exception {
    RestTemplate rest = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

    log.info("농가 데이터 등록 시작 startTime:{}", LocalDateTime.now());
    Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
        .stream()
        .forEach(line -> {
          String[] split = line.split(",");
          Farm bestFarm = Farm.builder()
              .address(split[1] + " " + split[2])
              .cropType(cropType)
              .farmType(farmType)
              .area(split[3])
              .build();
          farmRepository.save(bestFarm);

          String farmId = split[0];
          List<Facility> facilities = new ArrayList<>();
          ResponseEntity<String> responseEntity = rest.exchange(
              "http://apis.data.go.kr/1390000/SmartFarmdata/envdatarqst?serviceKey=" + serviceKey +
                  "&searchFrmhsCode=" + farmId + "&returnType=json&pageSize=100000", HttpMethod.GET,
              requestEntity, String.class);
          fromJsonToFacility(facilities, responseEntity.getBody(), bestFarm);
          facilityRepository.saveAll(facilities);

        });
    log.info("농가 데이터 등록 종료 endTime:{}", LocalDateTime.now());

  }

  public void fromJsonToFacility(List<Facility> facilities, String result, Farm bestFarm) {
    JSONObject jObject = new JSONObject(result);
    jObject = (JSONObject) jObject.get("response");
    jObject = (JSONObject) jObject.get("body");
    jObject = (JSONObject) jObject.get("items");
    JSONArray items = jObject.getJSONArray("item");
    for (int i = 0; i < items.length(); i++) {
      JSONObject itemJson = (JSONObject) items.get(i);
      FacilityRequestDto facilityRequestDto = FacilityRequestDto.of(itemJson, bestFarm);
      System.out.println(bestFarm.getId());
      facilities.add(modelMapper.map(facilityRequestDto, Facility.class));
    }
  }
}
