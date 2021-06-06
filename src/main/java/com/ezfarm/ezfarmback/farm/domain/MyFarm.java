package com.ezfarm.ezfarmback.farm.domain;

import com.ezfarm.ezfarmback.alarm.domain.AlarmRange;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class MyFarm extends Farm{

    @Column(nullable = false)
    private Boolean isMain;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    @Setter
    private LocalDateTime StartedDate;

    @ManyToOne
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private AlarmRange alarmRange;

    @Builder
    public MyFarm(Long id, Boolean isMain, String name, String address, String phoneNumber, FarmType farmType,
                Crop crop, String area, User user) {
        super(id, address, farmType, crop, area);
        this.isMain = isMain;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

}
