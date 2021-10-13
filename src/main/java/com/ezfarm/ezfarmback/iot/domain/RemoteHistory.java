package com.ezfarm.ezfarmback.iot.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RemoteHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "remote_history_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "farm_id")
  private Farm farm;

  private String value;

  public RemoteHistory(Farm farm, String value) {
    this.farm = farm;
    this.value = value;
  }

  public static List<RemoteHistory> create(Remote remote, RemoteRequest request) {
    List<RemoteHistory> histories = new ArrayList<>();
    if (remote.getWater().isNotEquals(request.getWater())) {
      histories.add(
          new RemoteHistory(
              remote.getFarm(),
              "급수 설정을 " + remote.getWater() + "에서 " + request.getWater() + "(으)로 변경하였습니다"
          ));
    }

    if (!remote.getTemperature().equals(request.getTemperature())) {
      histories.add(
          new RemoteHistory(
              remote.getFarm(),
              "온도 설정을 " + remote.getTemperature() + "에서 " + request.getTemperature()
                  + "(으)로 변경하였습니다"
          ));
    }

    if (remote.getIlluminance().isNotEquals(request.getIlluminance())) {
      histories.add(
          new RemoteHistory(
              remote.getFarm(),
              "조도 설정을 " + remote.getIlluminance() + "에서 " + request.getIlluminance()
                  + "(으)로 변경하였습니다"
          ));
    }

    if (remote.getCo2().isNotEquals(request.getCo2())) {
      histories.add(
          new RemoteHistory(
              remote.getFarm(),
              "Co2 설정을 " + remote.getCo2() + "에서 " + request.getCo2() + "(으)로 변경하였습니다"
          ));
    }
    return histories;
  }
}
