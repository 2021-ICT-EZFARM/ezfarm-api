package com.ezfarm.ezfarmback.facility.scheduler;

import com.ezfarm.ezfarmback.alert.domain.AlertFacilityType;
import com.ezfarm.ezfarmback.alert.domain.AlertRange;
import com.ezfarm.ezfarmback.alert.domain.AlertRangeRepository;
import com.ezfarm.ezfarmback.alert.domain.AlertType;
import com.ezfarm.ezfarmback.common.fcm.FcmEvent;
import com.ezfarm.ezfarmback.common.fcm.FcmService;
import com.ezfarm.ezfarmback.facility.domain.FacilityAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.domain.hour.FacilityRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Transactional
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class FacilityScheduler {

  private final FacilityRepository facilityRepository;
  private final FacilityDayAvgRepository facilityDayAvgRepository;
  private final FacilityWeekAvgRepository facilityWeekAvgRepository;
  private final FacilityMonthAvgRepository facilityMonthAvgRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final AlertRangeRepository alertRangeRepository;
  private final FcmService fcmService;
  private Map<AlertFacilityType, AlertType> alerts;

  @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
  public void checkFacilityScheduler() {
    alerts = new HashMap<>();

    List<Facility> facilities = facilityRepository.findByMeasureDateStartsWith(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH")));

    for (Facility facility : facilities) {
      AlertRange alertRange = alertRangeRepository.findByFarm(facility.getFarm())
          .orElseGet(() -> null);
      if (alertRange == null) {
        continue;
      }
      checkFacility(facility, alertRange);
      alerts.forEach((key, value) -> {
        if (fcmService.validateFcmToken(facility.getFarm().getUser().getId())) {
          eventPublisher.publishEvent(new FcmEvent(facility, key, value));
        }
      });
    }
  }

  private void checkFacility(Facility facility, AlertRange alertRange) {
    if (facility.getTmp() > alertRange.getTmpMax()) {
      alerts.put(AlertFacilityType.TMP, AlertType.EXCEED);
    } else if (facility.getTmp() < alertRange.getTmpMin()) {
      alerts.put(AlertFacilityType.TMP, AlertType.UNDER);
    }
    checkHumidity(facility, alertRange);
  }

  private void checkHumidity(Facility facility, AlertRange alertRange) {
    if (facility.getHumidity() > alertRange.getHumidityMax()) {
      alerts.put(AlertFacilityType.HUMIDITY, AlertType.EXCEED);
    } else if (facility.getHumidity() < alertRange.getHumidityMin()) {
      alerts.put(AlertFacilityType.HUMIDITY, AlertType.UNDER);
    }
    checkIlluminance(facility, alertRange);
  }

  private void checkIlluminance(Facility facility, AlertRange alertRange) {
    if (facility.getIlluminance() > alertRange.getImnMax()) {
      alerts.put(AlertFacilityType.IMN, AlertType.EXCEED);
    } else if (facility.getTmp() < alertRange.getImnMin()) {
      alerts.put(AlertFacilityType.IMN, AlertType.UNDER);
    }
    checkCo2(facility, alertRange);
  }

  private void checkCo2(Facility facility, AlertRange alertRange) {
    if (facility.getCo2() > alertRange.getCo2Max()) {
      alerts.put(AlertFacilityType.C02, AlertType.EXCEED);
    } else if (facility.getCo2() < alertRange.getCo2Min()) {
      alerts.put(AlertFacilityType.C02, AlertType.UNDER);
    }
    checkPh(facility, alertRange);
  }

  private void checkPh(Facility facility, AlertRange alertRange) {
    if (facility.getPh() > alertRange.getPhMax()) {
      alerts.put(AlertFacilityType.PH, AlertType.EXCEED);
    } else if (facility.getPh() < alertRange.getPhMin()) {
      alerts.put(AlertFacilityType.PH, AlertType.UNDER);
    }
    checkMos(facility, alertRange);
  }

  private void checkMos(Facility facility, AlertRange alertRange) {
    if (facility.getMos() > alertRange.getMosMax()) {
      alerts.put(AlertFacilityType.MOS, AlertType.EXCEED);
    } else if (facility.getMos() < alertRange.getMosMin()) {
      alerts.put(AlertFacilityType.MOS, AlertType.UNDER);
    }
  }


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
