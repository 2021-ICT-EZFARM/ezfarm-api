package com.ezfarm.ezfarmback.remote.domain;

import com.ezfarm.ezfarmback.common.BaseTimeEntity;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class RemoteHistory extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "remote_history_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "farm_id")
  private Farm farm;

  private String value;

  private boolean successYn;

  @Builder
  public RemoteHistory(Farm farm, String value) {
    this.farm = farm;
    this.value = value;
  }

  public void setSuccessYn(boolean successYn) {
    this.successYn = successYn;
  }
}
