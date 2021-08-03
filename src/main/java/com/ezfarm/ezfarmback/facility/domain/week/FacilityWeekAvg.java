package com.ezfarm.ezfarmback.facility.domain.week;


import com.ezfarm.ezfarmback.facility.domain.FacilityAvg;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
public class FacilityWeekAvg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Embedded
    private FacilityAvg facilityAvg;

    private String measureDate;

    @Builder
    public FacilityWeekAvg(Farm farm, FacilityAvg facilityAvg, LocalDateTime measureDate) {
        this.farm = farm;
        this.facilityAvg = facilityAvg;
        this.measureDate = measureDate.format(DateTimeFormatter.ofPattern("yyyy-")) + measureDate.get(
            WeekFields.of(Locale.KOREA).weekOfWeekBasedYear()) + "week";
    }
}
