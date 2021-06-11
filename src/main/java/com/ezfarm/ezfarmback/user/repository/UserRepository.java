package com.ezfarm.ezfarmback.user.repository;

import com.ezfarm.ezfarmback.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    void deleteAll();

    boolean existsByEmail(String email);
}
