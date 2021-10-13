package com.ezfarm.ezfarmback.alert.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Alert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private AlertType alertType;

    private AlertFacilityType alertFacilityType;

    private Boolean isChecked;

    @Builder
    public Alert(Farm farm, AlertType alertType, AlertFacilityType alertFacilityType) {
        this.farm = farm;
        this.alertType = alertType;
        this.alertFacilityType = alertFacilityType;
        this.isChecked = false;
    }

}
