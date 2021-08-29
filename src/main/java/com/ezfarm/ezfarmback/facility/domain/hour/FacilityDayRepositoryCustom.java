package com.ezfarm.ezfarmback.facility.domain.hour;

import com.ezfarm.ezfarmback.facility.dto.FacilityAvgSearchResponse;
import java.util.List;

public interface FacilityDayRepositoryCustom {

  List<FacilityAvgSearchResponse> findDayAvgGroupByFarm();

}
