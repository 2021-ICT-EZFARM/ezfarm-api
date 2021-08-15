package com.ezfarm.ezfarmback.favorite.dto;

import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {

    private Long favoriteId;

    private FarmSearchResponse farmSearchResponse;

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream().map(v ->
            new FavoriteResponse(v.getId(), FarmSearchResponse.favoriteOf(v))
        ).collect(Collectors.toList());
    }
}
