package com.ezfarm.ezfarmback.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  void deleteAll();

  boolean existsByEmail(String email);

  User findByName(String name);
}
