package com.ezfarm.ezfarmback.farm.domain;

import com.ezfarm.ezfarmback.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmRepository extends JpaRepository<Farm, Long> {

    List<Farm> findAllByUser(User user);

    Optional<Farm> findByIsMainAndUser(boolean isMain, User user);

}
