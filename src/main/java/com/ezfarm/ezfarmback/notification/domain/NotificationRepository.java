package com.ezfarm.ezfarmback.notification.domain;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByFarmOrderByCreatedDateDesc(Farm farm);
}
