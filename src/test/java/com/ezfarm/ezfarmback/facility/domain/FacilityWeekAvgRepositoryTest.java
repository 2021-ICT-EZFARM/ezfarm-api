package com.ezfarm.ezfarmback.facility.domain;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.config.AppConfig;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityWeekAvgRequest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("시설 주 평균 단위 테스트(Repository)")
@Import(AppConfig.class)
@DataJpaTest
public class FacilityWeekAvgRepositoryTest {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FacilityWeekAvgRepository facilityWeekAvgRepository;

    Farm savedFarm;

    FacilityWeekAvg monthOne, monthTwo, monthThr;

    @BeforeEach
    void setUp() {
        Farm farm = Farm.builder()
            .name("농가")
            .isMain(false)
            .build();
        savedFarm = farmRepository.save(farm);

        LocalDateTime jan = LocalDateTime.of(2018, 1, 10, 0, 0);
        LocalDateTime feb = LocalDateTime.of(2018, 2, 17, 0, 0);
        LocalDateTime oct = LocalDateTime.of(2018, 10, 28, 0, 0);

        monthOne = FacilityWeekAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(jan)
            .build();

        monthTwo = FacilityWeekAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(feb)
            .build();

        monthThr = FacilityWeekAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(oct)
            .build();

        facilityWeekAvgRepository.saveAll(asList(monthOne, monthTwo, monthThr));
    }

    @DisplayName("타 농가 주 평균 데이터를 조회한다.")
    @Test
    void findAllByFarmAndMeasureDateStartsWith_month() {
        FacilityWeekAvgRequest facilityWeekAvgRequest = new FacilityWeekAvgRequest("2018-01", null,
            null);

        List<FacilityWeekAvg> response = facilityWeekAvgRepository.findAllByFarmAndMeasureDateStartsWith(
            savedFarm, facilityWeekAvgRequest);

        Assertions.assertAll(
            () -> assertThat(response.size()).isEqualTo(1),
            () -> assertThat(response).extracting("measureDate")
                .contains(monthOne.getMeasureDate())
        );
    }
}
