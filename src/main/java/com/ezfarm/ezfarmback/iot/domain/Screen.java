package com.ezfarm.ezfarmback.iot.domain;

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
public class Screen {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "screen_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "farm_id")
  private Farm farm;

  private String imageUrl;

  private String cropCondition;

  private String measureTime;

  @Builder
  public Screen(Farm farm, String imageUrl, String cropCondition, String measureTime) {
    this.farm = farm;
    this.imageUrl = imageUrl;
    this.cropCondition = cropCondition;
    this.measureTime = measureTime;
  }
}
