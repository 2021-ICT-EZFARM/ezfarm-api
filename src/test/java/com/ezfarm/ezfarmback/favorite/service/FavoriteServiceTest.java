package com.ezfarm.ezfarmback.favorite.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.ezfarm.ezfarmback.favorite.domain.FavoriteRepository;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("즐겨찾기 단위 테스트(Service)")
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FarmRepository farmRepository;

    private FavoriteService favoriteService;

    private User loginUser;

    private User farmOwner;

    private Farm farm;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(farmRepository, favoriteRepository);
        loginUser = User.builder()
            .id(1L)
            .name("남상우")
            .email("a@gmail.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();

        farmOwner = User.builder()
            .id(2L)
            .name("홍길동")
            .email("b@gmail.com")
            .password("비밀번호")
            .role(Role.ROLE_USER)
            .build();

        farm = Farm.builder()
            .id(1L)
            .user(farmOwner)
            .address("경기")
            .name("테스트 농가")
            .phoneNumber("010-2222-2222")
            .area("100")
            .farmType(FarmType.GLASS)
            .cropType(CropType.PAPRIKA)
            .isMain(true)
            .build();
    }

    @DisplayName("농가 즐겨찾기를 추가한다.")
    @Test
    void addFavorite_success() {
        when(farmRepository.findById(any())).thenReturn(Optional.ofNullable(farm));
        when(favoriteRepository.findAllByUserAndFarm(any(), any())).thenReturn(emptyList());
        when(favoriteRepository.save(any())).thenReturn(any());

        favoriteService.addFavorite(loginUser, 1L);

        verify(favoriteRepository).save(any());
    }

    @DisplayName("즐겨찾기를 추가 시 존재하는 농가가 아니면 예외 처리한다.")
    @Test
    void addFavorite_failure_invalid_farm() {
        when(farmRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.addFavorite(loginUser, 1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.INVALID_FARM_ID.getMessage());
    }

    @DisplayName("즐겨찾기 추가 시 자신의 농가면 예외 처리한다.")
    @Test
    void addFavorite_failure_my_farm() {
        Farm myFarm = Farm.builder().user(loginUser).build();
        when(farmRepository.findById(any())).thenReturn(Optional.ofNullable(myFarm));

        assertThatThrownBy(() -> favoriteService.addFavorite(loginUser, 1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.MY_FARM_NOT_ALLOWED.getMessage());
    }

    @DisplayName("즐겨찾기 추가 시 중복되는 농가이면 예외 처리한다.")
    @Test
    void addFavorite_failure_duplicated_farm() {
        Favorite favorite = Favorite.builder()
            .farm(farm)
            .build();

        when(farmRepository.findById(any())).thenReturn(Optional.ofNullable(farm));
        when(favoriteRepository.findAllByUserAndFarm(any(), any()))
            .thenReturn(singletonList(favorite));

        assertThatThrownBy(() -> favoriteService.addFavorite(loginUser, 1L))
            .isInstanceOf(CustomException.class)
            .hasMessage(ErrorCode.FAVORITE_DUPLICATED.getMessage());
    }

    @DisplayName("농가 즐겨찾기를 조회한다.")
    @Test
    void findFavorites_success() {
        //farm.setUser(farmOwner);
        Favorite favorite = Favorite.builder()
            .user(farmOwner)
            .farm(farm)
            .build();

        when(favoriteRepository.findAllByUser(any())).thenReturn(
            singletonList(favorite));

        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(loginUser);

        Assertions.assertAll(
            () -> assertThat(favoriteResponses.size()).isEqualTo(1),
            () -> assertThat(favoriteResponses.get(0).getFarmSearchResponse().getName())
                .isEqualTo(farm.getName())
        );
    }

    @DisplayName("농가 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite_success() {
        favoriteService.deleteFavorite(1L);
        verify(favoriteRepository).deleteById(anyLong());
    }
}
