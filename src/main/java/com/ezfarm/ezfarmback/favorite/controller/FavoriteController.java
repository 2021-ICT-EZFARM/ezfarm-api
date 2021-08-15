package com.ezfarm.ezfarmback.favorite.controller;

import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.favorite.service.FavoriteService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "농가 즐겨찾기 API")
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @ApiOperation(value = "즐겨찾기 추가")
    @ApiResponses({
        @ApiResponse(code = 400, message = "이미 즐겨찾기에 등록된 농가입니다."),
        @ApiResponse(code = 400, message = "즐겨찾기는 최대 5개만 등록할 수 있습니다."),
        @ApiResponse(code = 400, message = "나의 농가는 즐겨찾기에 등록할 수 없습니다."),
        @ApiResponse(code = 404, message = "존재하지 않는 농가입니다.")
    })
    @PostMapping("/{farmId}")
    public ResponseEntity<Void> addFavorite(@CurrentUser User user, @PathVariable Long farmId) {
        favoriteService.addFavorite(user, farmId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "즐겨찾기 조회")
    @GetMapping
    public ResponseEntity<List<FarmSearchResponse>> findFavorites(@CurrentUser User user) {
        List<FarmSearchResponse> favoriteResponses = favoriteService.findFavorites(user);
        return ResponseEntity.ok(favoriteResponses);
    }

    @ApiOperation(value = "즐겨찾기 삭제")
    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(@RequestParam Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }

}
