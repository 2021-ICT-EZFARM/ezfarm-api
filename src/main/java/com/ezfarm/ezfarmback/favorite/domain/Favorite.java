package com.ezfarm.ezfarmback.favorite.domain;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Favorite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "favorite_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "farm_id")
  private Farm farm;

  @Builder
  public Favorite(User user, Farm farm) {
    this.user = user;
    this.farm = farm;
  }

  public static Favorite create(User loginUser, Farm farm) {
    return Favorite.builder()
        .user(loginUser)
        .farm(farm)
        .build();
  }

  public boolean isDuplicated(Farm farm) {
    return this.farm.getId().equals(farm.getId());
  }
}
