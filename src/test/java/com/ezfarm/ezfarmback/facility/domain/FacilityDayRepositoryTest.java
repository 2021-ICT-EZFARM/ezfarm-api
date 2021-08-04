package com.ezfarm.ezfarmback.facility.domain;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.config.AppConfig;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.dto.FacilityDailyAvgRequest;
import com.ezfarm.ezfarmback.facility.dto.FacilityPeriodResponse;
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

@DisplayName("시설 일 평균 단위 테스트(Repository)")
@Import(AppConfig.class)
@DataJpaTest
public class FacilityDayRepositoryTest {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FacilityDayAvgRepository facilityDayAvgRepository;

    Farm savedFarm;

    FacilityDayAvg min;

    FacilityDayAvg mid;

    FacilityDayAvg max;

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

        min = FacilityDayAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(minDate)
            .build();

        mid = FacilityDayAvg.builder()
            .farm(savedFarm)
            .facilityAvg(new FacilityAvg())
            .measureDate(midDate)
            .build();

        max = FacilityDayAvg.builder()
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
            () -> assertThat(response.getStartDate()).isEqualTo(min.getMeasureDate()),
            () -> assertThat(response.getEndDate()).isEqualTo(max.getMeasureDate())
        );
    }

    @DisplayName("타 농가 일 평균 데이터를 조회한다.")
    @Test
    void findAllByFarmAndMeasureDateStartsWith() {
        FacilityDailyAvgRequest facilityDailyAvgRequest = new FacilityDailyAvgRequest("2017", "01");
        String date = facilityDailyAvgRequest.getYear() + "-" + facilityDailyAvgRequest.getMonth();

        List<FacilityDayAvg> response = facilityDayAvgRepository.findAllByFarmAndMeasureDateStartsWith(
            savedFarm, date);

        Assertions.assertAll(
            () -> assertThat(response.size()).isEqualTo(2),
            () -> assertThat(response).extracting("measureDate")
                .contains(min.getMeasureDate(), mid.getMeasureDate())
        );
    }
}
