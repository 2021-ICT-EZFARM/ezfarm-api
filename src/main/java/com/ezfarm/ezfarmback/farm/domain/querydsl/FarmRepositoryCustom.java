package com.ezfarm.ezfarmback.farm.domain.querydsl;

import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FarmRepositoryCustom {

    Page<FarmSearchResponse> findByNotUserAndFarmSearchCond(User user,
        FarmSearchCond farmSearchCond,
        Pageable pageable);
}
