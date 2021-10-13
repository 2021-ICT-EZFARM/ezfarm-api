package com.ezfarm.ezfarmback.favorite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteRequest;
import com.ezfarm.ezfarmback.favorite.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("농가 즐겨찾기 단위 테스트(Controller)")
@WebMvcTest(controllers = FavoriteController.class)
public class FavoriteControllerTest extends CommonApiTest {

  @MockBean
  FavoriteService favoriteService;

  @DisplayName("농가 즐겨찾기를 추가한다.")
  @WithMockCustomUser
  @Test
  void addFavorite() throws Exception {
    FavoriteRequest favoriteRequest = new FavoriteRequest(1L);

    doNothing().when(favoriteService).addFavorite(any(), any());

    mockMvc.perform(post("/api/favorite", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(favoriteRequest)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("농가 즐겨찾기를 조회한다.")
  @WithMockCustomUser
  @Test
  void findFavorites() throws Exception {
    when(favoriteService.findFavorites(any())).thenReturn(any());
    mockMvc.perform(get("/api/favorite"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("농가 즐겨찾기를 삭제한다.")
  @WithMockCustomUser
  @Test
  void deleteFavorite() throws Exception {
    doNothing().when(favoriteService).deleteFavorite(any());
    mockMvc.perform(delete(String.format("/api/favorite?favoriteId=%d", 1L)))
        .andExpect(status().isNoContent())
        .andDo(print());
  }
}
