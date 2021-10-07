package com.ezfarm.ezfarmback.farm.service;

import com.ezfarm.ezfarmback.common.Pagination;
import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FarmService {

  private final FarmRepository farmRepository;

  public Long createFarm(User user, FarmRequest request) {
    validateFarmStartDate(request.getStartDate());
    updateMainFarm(user, request);
    Farm farm = Farm.create(user, request);
    return farmRepository.save(farm).getId();
  }

  private void validateFarmStartDate(LocalDate startDate) {
    if (startDate != null && startDate.isBefore(LocalDate.now())) {
      throw new CustomException(ErrorCode.INVALID_FARM_START_DATE);
    }
  }

  @Transactional(readOnly = true)
  public List<FarmResponse> findMyFarms(User user) {
    return farmRepository.findAllByUser(user).stream().map(FarmResponse::of)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public FarmResponse findMyFarm(Long farmId) {
    return FarmResponse.of(validateFarmIdAndGetFarm(farmId));
  }

  public void updateMyFarm(User user, Long farmId, FarmRequest request) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    farm.validateIsMyFarm(user);
    validateFarmStartDateToUpdate(farm.getCreatedDate(), request.getStartDate());
    updateMainFarm(user, request);
    farm.update(request);
  }

  private void validateFarmStartDateToUpdate(LocalDateTime createdDate, LocalDate startDate) {
    if (startDate != null && startDate.isBefore(createdDate.toLocalDate())) {
      throw new CustomException(ErrorCode.INVALID_FARM_START_DATE);
    }
  }

  public void updateMainFarm(User user, FarmRequest farmRequest) {
    if (farmRequest.isMain()) {
      Optional<Farm> mainFarm = farmRepository.findByUserAndIsMain(user, true);
      mainFarm.ifPresent(farm -> farm.setMain(false));
    }
  }

  public void deleteMyFarm(User user, Long farmId) {
    Farm farm = validateFarmIdAndGetFarm(farmId);
    farm.validateIsMyFarm(user);
    farmRepository.delete(farm);
  }

  public Farm validateFarmIdAndGetFarm(Long farmId) {
    return farmRepository.findById(farmId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));
  }

  @Transactional(readOnly = true)
  public List<FarmSearchResponse> findOtherFarms(User user, FarmSearchCond farmSearchCond,
      Pagination pagination) {
    PageRequest pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
    return farmRepository
        .findByNotUserAndNotFavoritesAndFarmSearchCond(user, farmSearchCond, pageable)
        .getContent();
  }
}
