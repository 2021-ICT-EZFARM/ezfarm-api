package com.ezfarm.ezfarmback.facility.scheduler;

import com.ezfarm.ezfarmback.facility.domain.FacilityAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.hour.FacilityRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Transactional
@Component
public class FacilityScheduler {

  private final FacilityRepository facilityRepository;

  private final FacilityDayAvgRepository facilityDayAvgRepository;

  private final FacilityWeekAvgRepository facilityWeekAvgRepository;

  private final FacilityMonthAvgRepository facilityMonthAvgRepository;

  //cron : seconds, minutes, hours, day of month, month, day of week
  @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
  public void facilityDayAvgScheduler() {
    log.info("{} start - 시설 데이터 일 평균 계산", LocalDateTime.now());
    List<FacilityAvgSearchResponse> responses = facilityRepository.findDayAvgGroupByFarm();
    List<FacilityDayAvg> facilityDayAvgs = responses.stream().map(response -> {
      FacilityAvg facilityAvg = new FacilityAvg(response);
      return FacilityDayAvg.builder()
          .farm(response.getFarm())
          .facilityAvg(facilityAvg)
          .measureDate(LocalDateTime.now())
          .build();
    }).collect(Collectors.toList());
    facilityDayAvgRepository.saveAll(facilityDayAvgs);
    log.info("{} end - 시설 데이터 일 평균 계산", LocalDateTime.now());
  }

  @Scheduled(cron = "0 0 0 ? * WED", zone = "Asia/Seoul")
  public void facilityWeekAvgScheduler() {
    log.info("{} start - 시설 데이터 주 평균 계산", LocalDateTime.now());
    List<FacilityAvgSearchResponse> responses = facilityWeekAvgRepository.findWeekAvgGroupByFarm();
    List<FacilityWeekAvg> facilityWeekAvgs = responses.stream().map(response -> {
      FacilityAvg facilityAvg = new FacilityAvg(response);
      return FacilityWeekAvg.builder()
          .farm(response.getFarm())
          .facilityAvg(facilityAvg)
          .measureDate(LocalDateTime.now())
          .build();
    }).collect(Collectors.toList());
    facilityWeekAvgRepository.saveAll(facilityWeekAvgs);
    log.info("{} end - 시설 데이터 주 평균 계산", LocalDateTime.now());
  }

  @Scheduled(cron = "0 10 0 1 * *", zone = "Asia/Seoul")
  public void facilityMonthAvgScheduler() {
    log.info("{} start - 시설 데이터 월 평균 계산", LocalDateTime.now());
    List<FacilityAvgSearchResponse> responses = facilityMonthAvgRepository
        .findMonthAvgGroupByFarm();
    List<FacilityMonthAvg> facilityMonthAvgs = responses.stream().map(response -> {
      FacilityAvg facilityAvg = new FacilityAvg(response);
      return FacilityMonthAvg.builder()
          .farm(response.getFarm())
          .facilityAvg(facilityAvg)
          .measureDate(LocalDateTime.now())
          .build();
    }).collect(Collectors.toList());
    facilityMonthAvgRepository.saveAll(facilityMonthAvgs);
    log.info("{} end - 시설 데이터 월 평균 계산", LocalDateTime.now());
  }
}
