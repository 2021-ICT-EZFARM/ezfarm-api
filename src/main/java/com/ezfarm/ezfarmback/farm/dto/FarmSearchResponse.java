package com.ezfarm.ezfarmback.farm.dto;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.favorite.domain.Favorite;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FarmSearchResponse {

    private Long farmId;
    private String name;
    private String address;
    private String area;
    private FarmType farmType;
    private CropType cropType;

    @QueryProjection
    public FarmSearchResponse(Long farmId, String name, String address, String area,
        FarmType farmType, CropType cropType) {
        this.farmId = farmId;
        this.name = name;
        this.address = address;
        this.area = area;
        this.farmType = farmType;
        this.cropType = cropType;
    }

    public static List<FarmSearchResponse> listOf(List<Favorite> favorites) {
        return favorites.stream().map(v ->
            new FarmSearchResponse(v.getFarm().getId(),
                v.getFarm().getName(),
                v.getFarm().getAddress(),
                v.getFarm().getArea(),
                v.getFarm().getFarmType(),
                v.getFarm().getCropType()
            )
        ).collect(Collectors.toList());
    }
}
