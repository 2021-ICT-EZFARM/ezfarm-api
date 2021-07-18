package com.ezfarm.ezfarmback.farm.domain.querydsl;

import static com.ezfarm.ezfarmback.common.query.QueryCondition.cropTypeEq;
import static com.ezfarm.ezfarmback.common.query.QueryCondition.farmTypeEq;
import static com.ezfarm.ezfarmback.farm.domain.QFarm.farm;

import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.farm.dto.QFarmSearchResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FarmRepositoryCustomImpl implements FarmRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FarmSearchResponse> findByNotUserAndFarmSearchCond(User user,
        FarmSearchCond farmSearchCond,
        Pageable pageable) {
        QueryResults<FarmSearchResponse> results = queryFactory
            .select(new QFarmSearchResponse(
                farm.id,
                farm.name,
                farm.address,
                farm.area,
                farm.phoneNumber,
                farm.farmType,
                farm.cropType,
                farm.startDate
            ))
            .from(farm)
            .where(
                farm.user.ne(user),
                farmTypeEq(farmSearchCond.getFarmType()),
                cropTypeEq(farmSearchCond.getCropType())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
