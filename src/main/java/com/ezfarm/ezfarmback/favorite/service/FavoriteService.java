package com.ezfarm.ezfarmback.favorite.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.ezfarm.ezfarmback.favorite.domain.FavoriteRepository;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FarmRepository farmRepository;

    private final FavoriteRepository favoriteRepository;

    public void addFavorite(User user, Long farmId) {
        Farm findFarm = farmRepository.findById(farmId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

        boolean isExist = favoriteRepository.existsByUserAndFarm(user, findFarm);
        if (isExist) {
            throw new CustomException(ErrorCode.FAVORITE_DUPLICATED);
        }

        Favorite favorite = Favorite.builder()
            .user(user)
            .farm(findFarm)
            .build();

        favoriteRepository.save(favorite);
    }

}
