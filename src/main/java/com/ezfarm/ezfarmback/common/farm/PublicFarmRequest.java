package com.ezfarm.ezfarmback.common.farm;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

/**
 * Api 데이터 inTp : 내부온도 inHd : 내부습도 measDtStr : 측정 일시 otmsuplyqy : 1회 급액량 acSlrdQy : 누적 일사량 frmhId :
 * 농가코드 cunt : 일 급액횟수 ph : 급액 ph outTp : 외부온도 outWs : 풍속 daysuplyqy : 일 급액량 inCo2 : 내부 co2 ec : 급액
 * ec
 */
@Builder
@Data
@Slf4j
public class PublicFarmRequest {

  private Farm farm;

  private float tmp;

  private float humidity;

  private float illuminance;

  private float co2;

  private float ph;

  private float mos;

  private LocalDateTime measureDate;

  public static PublicFarmRequest of(JSONObject jsonObject, Farm bestFarm) {
    String measDtStr = jsonObject.getString("measDtStr");
    String savedDate = null;
    String year = measDtStr.substring(0, 4);
    switch (year) {
      case "2017":
        savedDate = "2020" + measDtStr.substring(4);
        break;
      case "2018":
        savedDate = "2021" + measDtStr.substring(4);
        break;
      case "2019":
        savedDate = "2022" + measDtStr.substring(4);
        break;
    }

    return PublicFarmRequest.builder()
        .farm(bestFarm)
        .tmp(jsonObject.getFloat("inTp"))
        .humidity(jsonObject.getFloat("inHd"))
        .measureDate(LocalDateTime.parse(savedDate, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
        .illuminance(jsonObject.getFloat("acSlrdQy"))
        .ph(jsonObject.getFloat("ph"))
        .co2(jsonObject.getFloat("inCo2"))
        .mos(60)
        .build();
  }
}