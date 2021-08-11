package com.ezfarm.ezfarmback.screen.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.utils.iot.IotUtils;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.screen.domain.Screen;
import com.ezfarm.ezfarmback.screen.domain.ScreenRepository;
import com.ezfarm.ezfarmback.screen.dto.ScreenResponse;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ScreenService {

    private final IotUtils iotUtils;
    private final ScreenRepository screenRepository;
    private final FarmRepository farmRepository;
    private final ModelMapper modelMapper;

    public ScreenResponse findLiveScreen(User user, Long farmId) {
        Farm findFarm = farmRepository.findById(farmId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

        if (!findFarm.isMyFarm(user.getId())) {
            throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
        }

        String measureTime = iotUtils.getLiveScreen(findFarm.getId());
        Screen screen = screenRepository.findByFarmAndMeasureTime(findFarm, measureTime)
            .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_SCREEN));

        return modelMapper.map(screen, ScreenResponse.class);
    }

    public List<ScreenResponse> findTodayScreens(User user, Long farmId) {
        Farm findFarm = farmRepository.findById(farmId)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_FARM_ID));

        if (!findFarm.isMyFarm(user.getId())) {
            throw new CustomException(ErrorCode.FARM_ACCESS_DENIED);
        }

        //TODO
        // 1 2 3 <- 오늘 || (전체 조회) 전날 4  5 6
        String today = LocalDate.now().toString();
        List<Screen> screens = screenRepository
            .findByFarmAndMeasureTimeStartingWith(findFarm, today);

        return screens.stream().map(screen -> modelMapper.map(screen, ScreenResponse.class))
            .collect(
                Collectors.toList());
    }
}
