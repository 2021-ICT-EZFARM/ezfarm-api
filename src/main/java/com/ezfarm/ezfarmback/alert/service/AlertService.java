package com.ezfarm.ezfarmback.alert.service;

import com.ezfarm.ezfarmback.alert.domain.AlertRange;
import com.ezfarm.ezfarmback.alert.domain.AlertRangeRepository;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final AlertRangeRepository alertRangeRepository;

    private final FarmRepository farmRepository;

    private final ModelMapper modelMapper;

    public AlertRangeResponse findAlertRange(User user, Long farmId) {
        Farm findFarm = farmRepository.findById(farmId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

        if (findFarm.isNotPossibleToAccessFarm(user.getId())) {
            throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
        }

        AlertRange alertRange = alertRangeRepository.findByFarm(findFarm)
            .orElseGet(() -> {
                AlertRange savedAlertRange = new AlertRange(findFarm);
                return alertRangeRepository.save(savedAlertRange);
            });
        return modelMapper.map(alertRange, AlertRangeResponse.class);
    }

    public void updateAlertRange(User user, Long alertRangeId,
        AlertRangeRequest alertRangeRequest) {
        AlertRange alertRange = alertRangeRepository.findById(alertRangeId)
            .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        if (alertRange.getFarm().isNotPossibleToAccessFarm(user.getId())) {
            throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
        }

        alertRange.updateAlertRange(alertRangeRequest);
    }
}
