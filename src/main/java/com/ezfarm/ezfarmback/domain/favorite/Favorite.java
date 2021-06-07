package com.ezfarm.ezfarmback.domain.favorite;

import com.ezfarm.ezfarmback.domain.farm.Farm;
import com.ezfarm.ezfarmback.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
@Table(name = "farm_favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

}
