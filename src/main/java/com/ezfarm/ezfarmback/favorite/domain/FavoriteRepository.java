package com.ezfarm.ezfarmback.favorite.domain;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserAndFarm(User user, Farm farm);

    List<Favorite> findAllByUser(User user);

}
