package com.ezfarm.ezfarmback.farm.service;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.dto.FarmForm;
import com.ezfarm.ezfarmback.user.domain.User;
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

    public Farm createFarm(User user, FarmForm farmForm) {
        Farm farm = formMapping(farmForm);
        farm.addOwner(user);
        return farmRepository.save(farm);
    }

    public Farm updateFarm(User user, Long farmId, FarmForm farmForm) {
        Farm farm = checkException(user, farmId);
        checkStartDate(farm, farmForm);
        if (canSetMain(farmForm, farm)) {
            setMainFarm(user, farm);
        }
        farm.update(farmForm);
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

    private Farm formMapping(FarmForm farmForm) {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper.map(farmForm, Farm.class);
    }

    private boolean canSetMain(FarmForm farmForm, Farm farm) {
        return !farm.getIsMain() && farmForm.getIsMain();
    }

    private void setMainFarm(User user, Farm farm) {
        farmRepository.findByIsMainAndUser(true, user).ifPresent(mainFarm -> mainFarm.setMain(false));
        farm.setMain(true);
    }

    private void checkStartDate(Farm farm, FarmForm farmForm) {
        if (farmForm.getStartDate().isBefore(farm.getCreatedDate())) {
            throw new IllegalArgumentException("농가 재배 시작 일자가 잘못됬습니다.");
        }
    }
}
