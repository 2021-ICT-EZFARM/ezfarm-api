package com.ezfarm.ezfarmback.facility.domain.hour;

import static com.ezfarm.ezfarmback.facility.domain.hour.QFacility.facility;

import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import com.ezfarm.ezfarmback.facility.dto.QFacilityAvgSearchResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacilityDayRepositoryCustomImpl implements FacilityDayRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<FacilityAvgSearchResponse> findDayAvgGroupByFarm() {
    return queryFactory
        .select(new QFacilityAvgSearchResponse(
            facility.farm,
            facility.tmp.avg().floatValue(),
            facility.humidity.avg().floatValue(),
            facility.illuminance.avg().floatValue(),
            facility.co2.avg().floatValue(),
            facility.ph.avg().floatValue(),
            facility.mos.avg().floatValue()
        ))
        .from(facility)
        .where(
            facility.measureDate.between(LocalDateTime.now().minusHours(24), LocalDateTime.now())
        )
        .groupBy(facility.farm)
        .fetch();
  }

}
