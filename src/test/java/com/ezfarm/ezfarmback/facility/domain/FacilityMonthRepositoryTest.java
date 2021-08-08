package com.ezfarm.ezfarmback.facility.domain;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.config.AppConfig;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityMonthAvgRequest;
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

@DisplayName("시설 월 평균 단위 테스트(Repository)")
@Import(AppConfig.class)
@DataJpaTest
public class FacilityMonthRepositoryTest {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FacilityMonthAvgRepository facilityMonthAvgRepository;

    Farm savedFarm;

    FacilityMonthAvg yearOne, yearTwo;

    @BeforeEach
    void setUp() {
        Farm farm = Farm.builder()
            .name("농가")
            .isMain(false)
            .build();
        savedFarm = farmRepository.save(farm);

        LocalDateTime minDate = LocalDateTime.of(2017, 1, 10, 0, 0);
        LocalDateTime midDate = LocalDateTime.of(2017, 1, 20, 0, 0);
        LocalDateTime maxDate = LocalDateTime.of(2018, 1, 10, 0, 0);

        yearOne = FacilityMonthAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(minDate)
            .build();

        yearTwo = FacilityMonthAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(maxDate)
            .build();

        facilityMonthAvgRepository.saveAll(asList(yearOne, yearTwo));
    }

    @DisplayName("타 농가 월 평균 데이터를 조회한다.")
    @Test
    void findAllByFarmAndMeasureDateStartsWith_month() {
        FacilityMonthAvgRequest facilityMonthAvgRequest = new FacilityMonthAvgRequest("2017");

        List<FacilityMonthAvg> response = facilityMonthAvgRepository.findAllByFarmAndMeasureDateStartsWith(
            savedFarm, facilityMonthAvgRequest.getYear());

        Assertions.assertAll(
            () -> assertThat(response.size()).isEqualTo(1),
            () -> assertThat(response).extracting("measureDate")
                .contains(yearOne.getMeasureDate())
        );
    }
}
