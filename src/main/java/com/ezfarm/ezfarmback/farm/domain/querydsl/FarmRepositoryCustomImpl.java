package com.ezfarm.ezfarmback.farm.domain.querydsl;

import static com.ezfarm.ezfarmback.farm.domain.QFarm.farm;
import static com.ezfarm.ezfarmback.favorite.domain.QFavorite.favorite;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.farm.dto.QFarmSearchResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FarmRepositoryCustomImpl implements FarmRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<FarmSearchResponse> findByNotUserAndNotFavoritesAndFarmSearchCond(User user,
      FarmSearchCond farmSearchCond,
      Pageable pageable) {
    QueryResults<FarmSearchResponse> results = queryFactory
        .select(new QFarmSearchResponse(
            farm.id,
            farm.name,
            farm.address,
            farm.area,
            farm.farmType,
            farm.cropType
        ))
        .from(farm)
        .leftJoin(favorite)
        .on(farm.eq(favorite.farm), favorite.user.eq(user))
        .where(
            favorite.isNull(),
            farm.user.ne(user),
            farmGroupEq(farmSearchCond.getFarmGroup()),
            farmTypeEq(farmSearchCond.getFarmType()),
            cropTypeEq(farmSearchCond.getCropType())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();
    return new PageImpl<>(results.getResults(), pageable, results.getTotal());
  }

  public static BooleanExpression farmTypeEq(String farmType) {
    return farmType != null ? farm.farmType.eq(FarmType.valueOf(farmType)) : null;
  }

  public static BooleanExpression cropTypeEq(String cropType) {
    return cropType != null ? farm.cropType.eq(CropType.valueOf(cropType)) : null;
  }

  public static BooleanExpression farmGroupEq(String farmGroup) {
    return farmGroup != null ? farm.farmGroup.eq(FarmGroup.valueOf(farmGroup)) : null;
  }
}
