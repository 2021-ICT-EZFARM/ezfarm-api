package com.ezfarm.ezfarmback.favorite.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.favorite.service.FavoriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("즐겨찾기 단위 테스트(Controller)")
@WebMvcTest(controllers = FavoriteController.class)
public class FavoriteControllerTest extends CommonApiTest {

    @MockBean
    FavoriteService favoriteService;

    @DisplayName("농장 즐겨찾기를 추가한다.")
    @WithMockCustomUser
    @Test
    void addFavorite() throws Exception {
        doNothing().when(favoriteService).addFavorite(any(), any());

        mockMvc.perform(post("/api/favorite/{farmId}", 1L))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
