package com.ezfarm.ezfarmback.farm.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
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

    public Farm updateFarm(User user, Long farmId, FarmRequest farmRequest) {
        Farm farm = checkException(user, farmId);
        checkStartDate(farm, farmRequest);
        if (canSetMain(farmRequest, farm)) {
            setMainFarm(user, farm);
        }
        farm.update(farmRequest);
        return farm;
    }

    public void deleteFarm(User user, Long farmId) {
        Farm farm = checkException(user, farmId);
        farmRepository.delete(farm);
    }

    public List<Farm> viewAllFarms(User user) {
        List<Farm> farms = farmRepository.findAllByUser(user);
        return farms;
    }

    public Farm viewFarm(User user, Long farmId) {
        Farm farm = checkException(user, farmId);
        return farm;
    }

    private Farm checkException(User user, Long farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(
            () -> new IllegalArgumentException(farmId + "에 매핑되는 농가가 없습니다.")
        );
        if (farm.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("농가에 접근 권한이 없습니다.");
        }
        return farm;
    }

    private boolean canSetMain(FarmRequest farmRequest, Farm farm) {
        //return !farm.get && farmRequest.getIsMain();
        return true;
    }

    private void setMainFarm(User user, Farm farm) {
        farmRepository.findByIsMainAndUser(true, user)
            .ifPresent(mainFarm -> mainFarm.setMain(false));
        farm.setMain(true);
    }

    private void checkStartDate(Farm farm, FarmRequest farmRequest) {
/*
        if (farmRequest.getStartDate().isBefore(farm.getCreatedDate())) {
            throw new IllegalArgumentException("농가 재배 시작 일자가 잘못됬습니다.");
        }
 */
    }
}
