package com.ezfarm.ezfarmback.facility.domain.month;

import static com.ezfarm.ezfarmback.facility.domain.week.QFacilityWeekAvg.facilityWeekAvg;

import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import com.ezfarm.ezfarmback.facility.dto.QFacilityAvgSearchResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;

@RequiredArgsConstructor
public class FacilityMonthRepositoryCustomImpl implements FacilityMonthRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<FacilityAvgSearchResponse> findMonthAvgGroupByFarm() {
    return queryFactory
        .select(new QFacilityAvgSearchResponse(
            facilityWeekAvg.farm,
            facilityWeekAvg.facilityAvg.avgTmp.avg().floatValue(),
            facilityWeekAvg.facilityAvg.avgTmp.avg().floatValue(),
            facilityWeekAvg.facilityAvg.avgTmp.avg().floatValue(),
            facilityWeekAvg.facilityAvg.avgTmp.avg().floatValue(),
            facilityWeekAvg.facilityAvg.avgTmp.avg().floatValue(),
            facilityWeekAvg.facilityAvg.avgTmp.avg().floatValue()
        ))
        .from(facilityWeekAvg)
        .where(
            facilityWeekAvg.measureDate.like(LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-%")))
        )
        .groupBy(facilityWeekAvg.farm)
        .fetch();
  }

}
