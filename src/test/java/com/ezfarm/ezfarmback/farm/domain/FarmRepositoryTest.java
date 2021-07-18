package com.ezfarm.ezfarmback.farm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.config.AppConfig;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

@DisplayName("농가 단위 테스트(Repository)")
@Import(AppConfig.class)
@DataJpaTest
@Rollback
public class FarmRepositoryTest {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private UserRepository userRepository;

    private User loginUser;

    private User otherUser;

    private Farm loginUserStrawberryVinyl;

    private Farm otherUserPaprikaGlass;

    private Farm otherUserPaprikaVinyl;

    @BeforeEach
    void setUp() {
        loginUser = User.builder()
            .name("테스트 이름1")
            .email("test1@email.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();

        otherUser = User.builder()
            .name("테스트 이름2")
            .email("test2@email.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();

        loginUserStrawberryVinyl = Farm.builder()
            .name("농가1")
            .isMain(false)
            .farmType(FarmType.VINYL)
            .cropType(CropType.STRAWBERRY)
            .build();

        otherUserPaprikaGlass = Farm.builder()
            .name("농가2")
            .isMain(false)
            .farmType(FarmType.GLASS)
            .cropType(CropType.PAPRIKA)
            .build();

        otherUserPaprikaVinyl = Farm.builder()
            .name("농가3")
            .isMain(false)
            .farmType(FarmType.VINYL)
            .cropType(CropType.PAPRIKA)
            .build();
    }

    @DisplayName("농가 타입이 비닐인 타 농가를 조회한다.")
    @Test
    void findOtherFarms_farmType_vinyl() {
        User savedLoginUser = userRepository.save(loginUser);
        User savedOtherUser = userRepository.save(otherUser);
        loginUserStrawberryVinyl.setUser(savedLoginUser);
        otherUserPaprikaGlass.setUser(savedOtherUser);
        otherUserPaprikaVinyl.setUser(savedOtherUser);

        farmRepository.saveAll(
            Arrays.asList(loginUserStrawberryVinyl, otherUserPaprikaGlass, otherUserPaprikaVinyl));

        FarmSearchCond farmSearchCond = new FarmSearchCond(FarmType.VINYL, null);
        Page<FarmSearchResponse> result = farmRepository
            .findByNotUserAndFarmSearchCond(savedLoginUser, farmSearchCond, PageRequest.of(0, 10));

        Assertions.assertAll(
            () -> assertThat(result.getTotalElements()).isEqualTo(1),
            () -> assertThat(result.getTotalPages()).isEqualTo(1),
            () -> assertThat(result.getContent()).extracting("name")
                .contains(otherUserPaprikaVinyl.getName())
        );
    }

    @DisplayName("작물 타입이 파프리카인 타 농가를 조회한다.")
    @Test
    void findOtherFarms_cropType_paprika() {
        User savedLoginUser = userRepository.save(loginUser);
        User savedOtherUser = userRepository.save(otherUser);
        loginUserStrawberryVinyl.setUser(savedLoginUser);
        otherUserPaprikaGlass.setUser(savedOtherUser);
        otherUserPaprikaVinyl.setUser(savedOtherUser);

        farmRepository.saveAll(
            Arrays.asList(loginUserStrawberryVinyl, otherUserPaprikaGlass, otherUserPaprikaVinyl));

        FarmSearchCond farmSearchCond = new FarmSearchCond(null, CropType.PAPRIKA);

        Page<FarmSearchResponse> result = farmRepository
            .findByNotUserAndFarmSearchCond(savedLoginUser, farmSearchCond, PageRequest.of(0, 10));

        Assertions.assertAll(
            () -> assertThat(result.getTotalElements()).isEqualTo(2),
            () -> assertThat(result.getTotalPages()).isEqualTo(1),
            () -> assertThat(result.getContent()).extracting("name")
                .contains(otherUserPaprikaVinyl.getName(), otherUserPaprikaVinyl.getName())
        );
    }
}
