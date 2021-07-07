package com.ezfarm.ezfarmback.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RemoteResponse {

  private String values;

  private Boolean successYn;
}
