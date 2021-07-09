package com.ezfarm.ezfarmback.farm.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FarmService {

    private final FarmRepository farmRepository;

    private final ModelMapper modelMapper;

    public Long createFarm(User user, FarmRequest farmRequest) {
        confirmStartDateToCreateFarm(farmRequest);

        Farm farm = modelMapper.map(farmRequest, Farm.class);
        farm.setUser(user);

        return farmRepository.save(farm).getId();
    }

    private void confirmStartDateToCreateFarm(FarmRequest farmRequest) {
        LocalDate farmStartDate = farmRequest.getStartDate();
        if (farmStartDate != null) {
            if (farmStartDate.isBefore(LocalDate.now())) {
                throw new CustomException(ErrorCode.INVALID_FARM_START_DATE);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<FarmResponse> findMyFarms(User user) {
        List<Farm> farms = farmRepository.findAllByUser(user);
        return farms.stream()
            .map(farm -> modelMapper.map(farm, FarmResponse.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FarmResponse findMyFarm(Long farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID)
        );
        return modelMapper.map(farm, FarmResponse.class);
    }

    public void updateMyFarm(User user, Long farmId, FarmRequest farmRequest) {
        Farm farm = confirmAuthorityToAccessFarm(user, farmId);
        confirmStartDateToUpdateFarm(farm, farmRequest);
        if (farmRequest.isMain()) {
            Optional<Farm> prevMainFarm = farmRepository.findByIsMainAndUser(true, user);
            prevMainFarm.ifPresent(value -> value.setMain(false));
        }
        farm.update(farmRequest);
    }

    private void confirmStartDateToUpdateFarm(Farm farm, FarmRequest farmRequest) {
        if (farmRequest.getStartDate() != null) {
            if (farmRequest.getStartDate().isBefore(farm.getCreatedDate().toLocalDate())) {
                throw new CustomException(ErrorCode.INVALID_FARM_START_DATE);
            }
        }
    }

    public void deleteMyFarm(User user, Long farmId) {
        Farm farm = confirmAuthorityToAccessFarm(user, farmId);
        farmRepository.delete(farm);
    }

    private Farm confirmAuthorityToAccessFarm(User user, Long farmId) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_FARM_ID)
        );
        if (!farm.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
        }
        return farm;
    }
}
