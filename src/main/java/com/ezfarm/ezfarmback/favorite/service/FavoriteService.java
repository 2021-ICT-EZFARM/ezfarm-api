package com.ezfarm.ezfarmback.favorite.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.ezfarm.ezfarmback.favorite.domain.FavoriteRepository;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteRequest;
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

  public void addFavorite(User user, FavoriteRequest request) {
    Farm farm = validateFarmIdAndGetFarm(request.getFarmId());
    List<Favorite> favorites = favoriteRepository.findAllByUserAndFarm(user, farm);
    validateIsExceedFavoriteMaximum(favorites);
    validateIsDuplicatedFavorite(farm, favorites);
    favoriteRepository.save(Favorite.create(user, farm));
  }

  public Farm validateFarmIdAndGetFarm(Long farmId) {
    return farmRepository.findById(farmId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));
  }

  private void validateIsExceedFavoriteMaximum(List<Favorite> favorites) {
    if (favorites.size() > 5) {
      throw new CustomException(ErrorCode.EXCEED_MAXIMUM_FAVORITE);
    }
  }

  private void validateIsDuplicatedFavorite(Farm farm, List<Favorite> favorites) {
    for (Favorite favorite : favorites) {
      if (favorite.isDuplicated(farm)) {
        throw new CustomException(ErrorCode.DUPLICATED_FAVORITE);
      }
    }
  }

  @Transactional(readOnly = true)
  public List<FavoriteResponse> findFavorites(User user) {
    return FavoriteResponse.listOf(favoriteRepository.findAllByUser(user));
  }

  public void deleteFavorite(Long favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }
}
