package com.ezfarm.ezfarmback.common.fcm;

import com.ezfarm.ezfarmback.alert.domain.AlertFacilityType;
import com.ezfarm.ezfarmback.alert.domain.AlertType;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmEvent {

  private Facility facility;
  private AlertFacilityType alertFacilityType;
  private AlertType alertType;
}
