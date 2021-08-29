package com.ezfarm.ezfarmback.facility.domain.month;

import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import java.util.List;

public interface FacilityMonthRepositoryCustom {

  List<FacilityAvgSearchResponse> findMonthAvgGroupByFarm();
}
