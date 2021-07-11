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
import java.util.stream.Collectors;
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

        if (findFarm.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.MY_FARM_NOT_ALLOWED);
        }

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

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(User user) {
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
