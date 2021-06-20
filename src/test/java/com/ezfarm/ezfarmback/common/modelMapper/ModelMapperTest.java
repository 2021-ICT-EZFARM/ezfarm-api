package com.ezfarm.ezfarmback.common.modelMapper;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModelMapper 매핑 테스트")
public class ModelMapperTest {

  ModelMapper modelMapper;

  FarmRequest farmRequest;

  FarmResponse farmResponse;

  Farm farm;

  @BeforeEach
  void setUp() {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT)
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(AccessLevel.PRIVATE)
        .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
        .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);

    farmRequest = new FarmRequest(
        "경기",
        "010-2222-2222",
        "100",
        true,
        FarmType.GLASS,
        CropType.PAPRIKA,
        null
    );

    farm = Farm.builder()
        .address("서울")
        .phoneNumber("010-1111-1111")
        .area("200")
        .farmType(FarmType.VINYL)
        .cropType(CropType.TOMATO)
        .isMain(true)
        .startDate(null)
        .build();

  }

  @DisplayName("Request객체를 entity로 매핑한다.")
  @Test
  void request_to_entity_success() {
    farm = modelMapper.map(farmRequest, Farm.class);

    assertEquals("경기", farm.getAddress());
    assertEquals("010-2222-2222", farm.getPhoneNumber());
    assertEquals("100", farm.getArea());
    assertEquals(FarmType.GLASS, farm.getFarmType());
    assertEquals(CropType.PAPRIKA, farm.getCropType());
  }

  @DisplayName("Entity를 Response객체로 매핑한다.")
  @Test
  void entity_to_response_success() {
    farmResponse = modelMapper.map(farm, FarmResponse.class);

    assertEquals("서울", farm.getAddress());
    assertEquals("010-1111-1111", farm.getPhoneNumber());
    assertEquals("200", farm.getArea());
    assertEquals(FarmType.VINYL, farm.getFarmType());
    assertEquals(CropType.TOMATO, farm.getCropType());
  }

  @DisplayName("Source가 null인 경우 예외가 발생한다.")
  @Test
  void failure_source_is_null() {
    FarmResponse farmResponse = null;
    assertThatThrownBy(() -> modelMapper.map(farmResponse, Farm.class))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("source cannot be null");
  }

  @DisplayName("Destination이 null인 경우 예외가 발생한다.")
  @Test
  void failure_destination_is_null() {
    Map<String, String> mismatchDto = new HashMap<>();
    assertThatThrownBy(() -> modelMapper.map(farmResponse, mismatchDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("source cannot be null");
  }

  @DisplayName("필드명이 다른 경우 무시한다.")
  @Test
  void ignore_mismatchedField_success() {
    Map<String, String> mismatchDto = new HashMap<>();
    mismatchDto.put("address", "부산");
    mismatchDto.put("tel", "010-3333-3333");
    mismatchDto.put("farmType", String.valueOf(FarmType.GLASS));

    farm = modelMapper.map(mismatchDto, Farm.class);
    assertEquals("부산", farm.getAddress());
    assertNotEquals("010-3333-3333", farm.getPhoneNumber());
    assertEquals(null, farm.getPhoneNumber());
    assertEquals(FarmType.GLASS, farm.getFarmType());
  }
}
