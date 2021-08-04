package com.ezfarm.ezfarmback.facility.domain;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.config.AppConfig;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("시설 일 평균 단위 테스트(Repository)")
@Import(AppConfig.class)
@DataJpaTest
public class FacilityDayRepositoryTest {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FacilityDayAvgRepository facilityDayAvgRepository;

    Farm savedFarm;

    @BeforeEach
    void setUp() {
        Farm farm = Farm.builder()
            .name("농가")
            .isMain(false)
            .build();
        savedFarm = farmRepository.save(farm);

        LocalDateTime minDate = LocalDateTime.of(2017, 1, 10, 0, 0);
        LocalDateTime midDate = LocalDateTime.of(2017, 5, 10, 0, 0);
        LocalDateTime maxDate = LocalDateTime.of(2018, 1, 10, 0, 0);

        FacilityDayAvg min = FacilityDayAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(minDate)
            .build();

        FacilityDayAvg mid = FacilityDayAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(midDate)
            .build();

        FacilityDayAvg max = FacilityDayAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(maxDate)
            .build();

        facilityDayAvgRepository.saveAll(asList(min, mid, max));
    }

    @DisplayName("검색 가능한 기간을 조회한다.")
    @Test
    void findMinAndMinMeasureDate() {
        FacilityPeriodResponse response = facilityDayAvgRepository.findMinAndMaxMeasureDateByFarm(
            savedFarm);

        Assertions.assertAll(
            () -> assertThat(response.getStartDate()).isEqualTo("2017-01-10"),
            () -> assertThat(response.getEndDate()).isEqualTo("2018-01-10")
        );
    }
}
