package com.ezfarm.ezfarmback.favorite.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.ezfarm.ezfarmback.favorite.domain.FavoriteRepository;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.List;
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

        /*
        if (findFarm.isMyFarm(user.getId())) {
            throw new CustomException(ErrorCode.MY_FARM_NOT_ALLOWED);
        }*/

        List<Favorite> favorites = favoriteRepository.findAllByUserAndFarm(user, findFarm);
        confirmFavoriteMaximumNumber(favorites);
        confirmSameFavorite(findFarm, favorites);

        Favorite favorite = Favorite.builder()
            .user(user)
            .farm(findFarm)
            .build();

        favoriteRepository.save(favorite);
    }

    private void confirmFavoriteMaximumNumber(List<Favorite> favorites) {
        if (favorites.size() > 5) {
            throw new CustomException(ErrorCode.EXCEED_FAVORITE_SIZE);
        }
    }

    private void confirmSameFavorite(Farm findFarm, List<Favorite> favorites) {
        for (Favorite favorite : favorites) {
            if (favorite.getFarm().isSameFarm(findFarm.getId())) {
                throw new CustomException(ErrorCode.FAVORITE_DUPLICATED);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(User user) {
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        return FavoriteResponse.listOf(favorites);
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
