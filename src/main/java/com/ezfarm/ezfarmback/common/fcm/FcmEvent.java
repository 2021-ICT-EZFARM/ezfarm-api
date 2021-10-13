package com.ezfarm.ezfarmback.common.fcm;

import com.ezfarm.ezfarmback.alert.domain.AlertFacilityType;
import com.ezfarm.ezfarmback.alert.domain.AlertType;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import lombok.Getter;

@Getter
public class FcmEvent {

  private Facility facility;
  private AlertFacilityType alertFacilityType;
  private AlertType alertType;

  public FcmEvent(Facility facility, AlertFacilityType alertFacilityType, AlertType alertType) {
    this.facility = facility;
    this.alertFacilityType = alertFacilityType;
    this.alertType = alertType;
  }

}
