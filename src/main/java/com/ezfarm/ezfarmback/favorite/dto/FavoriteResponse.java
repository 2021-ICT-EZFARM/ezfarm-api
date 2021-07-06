package com.ezfarm.ezfarmback.favorite.dto;

import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.ezfarm.ezfarmback.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FavoriteResponse {

    private Long id;
    private FarmResponse farmResponse;
    private FarmOwnerResponse farmOwnerResponse;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
            favorite.getId(),
            FarmResponse.of(favorite.getFarm()),
            FarmOwnerResponse.of(favorite.getFarm().getUser())
        );
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class FarmOwnerResponse {

        private String email;
        private String name;
        private String phoneNumber;
        private String address;
        private String imageUrl;

        public static FarmOwnerResponse of(User user) {
            return new FarmOwnerResponse(
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getImageUrl()
            );
        }
    }
}
