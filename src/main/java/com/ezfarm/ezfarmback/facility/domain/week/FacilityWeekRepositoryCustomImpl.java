package com.ezfarm.ezfarmback.facility.domain.week;

import static com.ezfarm.ezfarmback.facility.domain.week.QFacilityWeekAvg.facilityWeekAvg;

import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FacilityWeekRepositoryCustomImpl implements FacilityWeekRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FacilityWeekAvg> findAllByFarmAndMeasureDateStartsWith(Farm farm,
        FacilityWeekAvgRequest facilityWeekAvgRequest) {
        return queryFactory.selectFrom(facilityWeekAvg)
            .where(
                facilityWeekAvg.farm.eq(farm),
                (
                    measureDateEq(facilityWeekAvgRequest.getDateOne())
                        .or(measureDateEq(facilityWeekAvgRequest.getDateTwo()))
                        .or(measureDateEq(facilityWeekAvgRequest.getDateThr()))
                )
            ).fetch();
    }

    public static BooleanExpression measureDateEq(String measureDate) {
        return measureDate != null ? facilityWeekAvg.measureDate.like(measureDate + "%") : null;
    }
}
