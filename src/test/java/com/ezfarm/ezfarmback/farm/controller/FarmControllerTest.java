package com.ezfarm.ezfarmback.farm.controller;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmForm;
import com.ezfarm.ezfarmback.farm.service.FarmService;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class FarmControllerTest {

    @Autowired
    FarmRepository farmRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    FarmService farmService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    private User user;
    private Farm farm1;
    private Farm farm2;


    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .name("user")
                .email("test@gmail.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);

        farm1 = Farm.builder()
                .address("서울시")
                .phoneNumber("010")
                .area("100평")
                .isMain(false)
                .farmType(FarmType.GLASS)
                .cropType(CropType.PAPRIKA)
                .startDate(LocalDateTime.now())
                .user(user)
                .build();

        farmRepository.save(farm1);

        farm2 = Farm.builder()
                .address("부산시")
                .phoneNumber("017")
                .area("200평")
                .isMain(true)
                .farmType(FarmType.VINYL)
                .cropType(CropType.TOMATO)
                .startDate(LocalDateTime.now())
                .user(null)
                .build();

        farmRepository.save(farm2);
    }

    @AfterEach
    void AfterEach() {
        farmRepository.deleteAll();
        userRepository.deleteAll();
    }

    @WithMockCustomUser
    @DisplayName("농가 생성")
    @Test
    void createFarm_success() throws Exception {
        User user2 = createUserForFarm2();
        Farm newFarm = createFarm();
        mockMvc.perform(post("/api/farm")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modelMapper.map(newFarm, FarmForm.class))))
                .andExpect(status().isOk());

        assertEquals(farmRepository.findAllByUser(user).stream().count(), 2);

        Farm newFarm2 = createFarm();
        farmService.createFarm(user2, modelMapper.map(newFarm2, FarmForm.class));
        assertEquals(farmRepository.findAllByUser(user2).stream().count(), 2);
    }

    @WithMockCustomUser
    @DisplayName("모든 농가 조회")
    @Test
    void findAllFarms_success() throws Exception {
        Farm farm3 = Farm.builder()
                .address("대구시")
                .phoneNumber("011")
                .area("300평")
                .isMain(false)
                .farmType(FarmType.GLASS)
                .cropType(CropType.PAPRIKA)
                .startDate(LocalDateTime.now())
                .user(user)
                .build();
        farmRepository.save(farm3);
        List<Farm> farms = farmService.viewAllFarms(user);
        mockMvc.perform(get("/api/farm"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(farms)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        assertTrue(farms.contains(farm1));
        assertFalse(farms.contains(farm2));
        assertTrue(farms.contains(farm3));
    }

    @WithMockCustomUser
    @DisplayName("농가 조회 실패_invalid id")
    @Test
    void findFarm_invalidId() throws Exception {
        mockMvc.perform(get("/api/farm/123"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("123에 매핑되는 농가가 없습니다.",
                        result.getResolvedException().getMessage()));

        assertThrows(IllegalArgumentException.class, () -> {
            farmService.viewFarm(user, 123L);
        });
    }

    @WithMockCustomUser
    @DisplayName("농가 조회 실패_not owner")
    @Test
    void findFarm_notOwner() throws Exception {
        User user2 = createUserForFarm2();

        mockMvc.perform(get("/api/farm/2"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("농가에 접근 권한이 없습니다.",
                        result.getResolvedException().getMessage()));

        assertThrows(IllegalArgumentException.class, () -> {
            farmService.viewFarm(user, 2L);
        });
    }

    @WithAnonymousUser
    @DisplayName("농가 조회 실패_인증되지 않은 유저")
    @Test
    void findFarm_invalid_user() throws Exception {
        mockMvc.perform(get("/api/farm/1"))
                .andExpect(status().is(401))
                .andExpect(unauthenticated());
    }

    @WithMockCustomUser
    @DisplayName("농가 조회 성공")
    @Test
    void findFarm_success() throws Exception {
        mockMvc.perform(get("/api/farm/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(farm1)));

        Farm farm = farmService.viewFarm(user,1L);
        assertEquals(farm1, farm);
    }

    @WithMockCustomUser
    @DisplayName("농가 업데이트 실패_invalid id")
    @Test
    void updateFarm_invalidId() throws Exception {
        Farm updateFarm = createFarm();
        mockMvc.perform(patch("/api/farm/123")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modelMapper.map(updateFarm, FarmForm.class))))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("123에 매핑되는 농가가 없습니다.",
                        result.getResolvedException().getMessage()));

        assertThrows(IllegalArgumentException.class, () -> {
            farmService.viewFarm(user, 123L);
        });
    }

    @WithMockCustomUser
    @DisplayName("농가 업데이트 실패_not owner")
    @Test
    void updateFarm_notOwner() throws Exception {
        User user2 = createUserForFarm2();
        Farm updateFarm = createFarm();

        mockMvc.perform(patch("/api/farm/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modelMapper.map(updateFarm, FarmForm.class))))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("농가에 접근 권한이 없습니다.",
                        result.getResolvedException().getMessage()));


        assertThrows(IllegalArgumentException.class, () -> {
            farmService.updateFarm(user, 2L, modelMapper.map(updateFarm, FarmForm.class));
        });
        assertEquals(farm2.getAddress(), "부산시");
        assertEquals(farm2.getPhoneNumber(), "017");
        assertEquals(farm2.getArea(), "200평");
        assertEquals(farm2.getIsMain(), true);
        assertEquals(farm2.getFarmType(), FarmType.VINYL);
        assertEquals(farm2.getCropType(), CropType.TOMATO);
    }

    @WithMockCustomUser
    @DisplayName("농가 업데이트 실패_농가 재배 시작 일자 오류")
    @Test
    void updateFarm_startDateError() throws Exception {
        farm1.setCreatedDate(LocalDateTime.now().plusDays(5));
        Farm updateFarm = createFarm();
        mockMvc.perform(patch("/api/farm/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modelMapper.map(updateFarm, FarmForm.class))))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("농가 재배 시작 일자가 잘못됬습니다.",
                        result.getResolvedException().getMessage()));
    }

    @WithMockCustomUser
    @DisplayName("농가 업데이트 성공")
    @Test
    void updateFarm_success() throws Exception {
        farm1.setCreatedDate(LocalDateTime.now());
        Farm updateFarm = createFarm();
        mockMvc.perform(patch("/api/farm/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modelMapper.map(updateFarm, FarmForm.class))))
                .andExpect(status().isOk());

        Farm updateFarm1 = farmRepository.findById(1L).get();
        assertNotEquals(updateFarm1.getAddress(), "서울시");
        assertNotEquals(updateFarm1.getPhoneNumber(), "010");
        assertNotEquals(updateFarm1.getArea(), "100평");
        assertNotEquals(updateFarm1.getIsMain(), false);
        assertNotEquals(updateFarm1.getFarmType(), FarmType.GLASS);
        assertNotEquals(updateFarm1.getCropType(), CropType.PAPRIKA);
    }

    @WithMockCustomUser
    @DisplayName("농가 업데이트 성공_메인 농가 변경")
    @Test
    void updateFarm_success_changeMain() throws Exception {
        farm1.setCreatedDate(LocalDateTime.now());
        Farm updateFarm = createFarm();

        farmRepository.save(updateFarm);
        mockMvc.perform(patch("/api/farm/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(modelMapper.map(updateFarm, FarmForm.class))))
                .andExpect(status().isOk());

        assertEquals(farm1.getIsMain(), true);
        assertEquals(updateFarm.getIsMain(), false);
    }

    @WithMockCustomUser
    @DisplayName("농가 삭제 실패_invalid id")
    @Test
    void deleteFarm_invalidId() throws Exception {
        mockMvc.perform(delete("/api/farm/123"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("123에 매핑되는 농가가 없습니다.",
                        result.getResolvedException().getMessage()));

        assertThrows(IllegalArgumentException.class, () -> {
            farmService.deleteFarm(user, 123L);
        });
    }

    @WithMockCustomUser
    @DisplayName("농가 삭제 실패_not owner")
    @Test
    void deleteFarm_notOwner() throws Exception {
        User user2 = createUserForFarm2();

        mockMvc.perform(delete("/api/farm/2"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("농가에 접근 권한이 없습니다.",
                        result.getResolvedException().getMessage()));

        assertThrows(IllegalArgumentException.class, () -> {
            farmService.deleteFarm(user, 123L);
        });

        assertTrue(farmRepository.existsById(2L));
    }

    @WithMockCustomUser
    @DisplayName("농가 삭제 성공")
    @Test
    void deleteFarm_success() throws Exception {
        User user2 = createUserForFarm2();

        mockMvc.perform(delete("/api/farm/1"))
                .andExpect(status().isOk());

        assertFalse(farmRepository.findAll().contains(farm1));

        farmService.deleteFarm(user2, 2L);
        assertFalse(farmRepository.findAll().contains(farm2));
    }

    private User createUserForFarm2() {
        User user2 = User.builder()
                .name("user2")
                .email("test@gmail.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        user2.setCreatedDate(LocalDateTime.now());
        userRepository.save(user2);
        farm2.addOwner(user2);
        return user2;
    }

    private Farm createFarm() {
        return Farm.builder()
                .address("대구시")
                .phoneNumber("011")
                .area("300평")
                .isMain(true)
                .farmType(FarmType.VINYL)
                .cropType(CropType.TOMATO)
                .startDate(LocalDateTime.now().plusDays(3))
                .user(user)
                .build();
    }
}