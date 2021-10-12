package com.ezfarm.ezfarmback.alert.service;

import com.ezfarm.ezfarmback.alert.domain.AlertRange;
import com.ezfarm.ezfarmback.alert.domain.AlertRangeRepository;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.alert.dto.AlertRequest;
import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.fcm.FcmListener;
import com.ezfarm.ezfarmback.common.fcm.FcmService;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final AlertRangeRepository alertRangeRepository;

    private final FarmRepository farmRepository;

    private final ModelMapper modelMapper;

    private final FcmService fcmService;

    public final Map<Long, String> tokenMap = new HashMap<>();

    public AlertRangeResponse findAlertRange(User user, Long farmId) {
        Farm findFarm = farmRepository.findById(farmId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

        if (!findFarm.isMyFarm(user.getId())) {
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

        if (!alertRange.getFarm().isMyFarm(user.getId())) {
            throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
        }

        alertRange.updateAlertRange(alertRangeRequest);
    }

    public void register(Long userId, String token) {
        tokenMap.put(userId, token);
    }

    public void deleteToken(Long userId) {
        tokenMap.remove(userId);
    }

    public String getToken(Long userId) {
        return tokenMap.get(userId);
    }

    public boolean checkToken(Long userId) {
        return tokenMap.containsKey(userId);
    }

    public void sendNotification(AlertRequest request) {
        try {
            fcmService.send(request);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
