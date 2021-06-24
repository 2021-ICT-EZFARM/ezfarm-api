package com.ezfarm.ezfarmback.farm.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FarmService {

    private final FarmRepository farmRepository;

    private final ModelMapper modelMapper;

    public Long createFarm(User user, FarmRequest farmRequest) {

        confirmFarmStartDate(farmRequest);

        Farm farm = modelMapper.map(farmRequest, Farm.class);
        farm.addOwner(user);

        Farm saveFarm = farmRepository.save(farm);
        return saveFarm.getId();
    }

    private void confirmFarmStartDate(FarmRequest farmRequest) {
        LocalDate farmStartDate = farmRequest.getStartDate();
        if (farmStartDate != null) {
            if (farmStartDate.isBefore(LocalDate.now())) {
                throw new CustomException(ErrorCode.INVALID_FARM_START_DATE);
            }
        }
    }

    public void updateFarm(User user, Long farmId, FarmRequest farmRequest) {
        Farm farm = checkException(user, farmId);
        checkStartDate(farm, farmRequest);
        if (farmRequest.isMain()) {
            Optional<Farm> previousMain = farmRepository.findByIsMainAndUser(true, user);
            if (previousMain.isPresent()) {
                previousMain.get().setMain(false);
            }
        }
        farm.setMain(farmRequest.isMain());
        farm.update(farmRequest);
    }

    public void deleteFarm(User user, Long farmId) {
        Farm farm = checkException(user, farmId);
        farmRepository.delete(farm);
    }

    public List<FarmResponse> viewAllFarms(User user) {
        List<Farm> farms = farmRepository.findAllByUser(user);
        List<FarmResponse> farmResponses = farms.stream()
            .map(farm -> modelMapper.map(farm, FarmResponse.class))
            .collect(Collectors.toList());
        return farmResponses;
    }

    public FarmResponse viewFarm(User user, Long farmId) {
        Farm farm = checkException(user, farmId);
        return modelMapper.map(farm, FarmResponse.class);
    }

    private Farm checkException(User user, Long farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID)
        );
        if (farm.getUser().getId() != user.getId()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return farm;
    }

    private void checkStartDate(Farm farm, FarmRequest farmRequest) {
        if (farmRequest.getStartDate().isBefore(farm.getCreatedDate().toLocalDate())) {
            throw new CustomException(ErrorCode.INVALID_FARM_START_DATE);
        }
    }
}
