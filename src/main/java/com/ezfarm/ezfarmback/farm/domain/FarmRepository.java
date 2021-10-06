package com.ezfarm.ezfarmback.farm.domain;

import com.ezfarm.ezfarmback.farm.domain.querydsl.FarmRepositoryCustom;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, Long>, FarmRepositoryCustom {

  List<Farm> findAllByUser(User user);

  Optional<Farm> findByUserAndIsMain(User user, boolean main);
}
