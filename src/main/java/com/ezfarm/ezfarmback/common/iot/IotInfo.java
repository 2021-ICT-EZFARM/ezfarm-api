package com.ezfarm.ezfarmback.common.iot;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class IotInfo {

  @Value("${property.iot.hostname}")
  private String hostname;

  @Value("${property.iot.username}")
  private String username;

  @Value("${property.iot.password}")
  private String password;
}
