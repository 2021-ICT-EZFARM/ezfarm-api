package com.ezfarm.ezfarmback.facility.domain;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.config.AppConfig;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("시설 월 평균 단위 테스트(Repository)")
@Import(AppConfig.class)
@DataJpaTest
public class FacilityMonthRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FacilityMonthAvgRepository facilityMonthAvgRepository;

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

        FacilityMonthAvg min = FacilityMonthAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(minDate)
            .build();

        FacilityMonthAvg mid = FacilityMonthAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(midDate)
            .build();

        FacilityMonthAvg max = FacilityMonthAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(maxDate)
            .build();

        facilityMonthAvgRepository.saveAll(asList(min, mid, max));
    }

    @DisplayName("검색 가능한 기간을 조회한다.")
    @Test
    void findMinAndMinMeasureDate() {
        FacilityPeriodResponse response = facilityMonthAvgRepository.findMinAndMinMeasureDateByFarm(
            savedFarm);

        Assertions.assertAll(
            () -> assertThat(response.getStartDate()).isEqualTo("2017-01"),
            () -> assertThat(response.getEndDate()).isEqualTo("2018-01")
        );
    }
}
