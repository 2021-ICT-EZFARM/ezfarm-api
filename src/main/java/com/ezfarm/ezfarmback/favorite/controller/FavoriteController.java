package com.ezfarm.ezfarmback.favorite.controller;

import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.favorite.service.FavoriteService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
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

@Api(tags = "즐겨찾기 API")
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{farmId}")
    public ResponseEntity<Void> addFavorite(@CurrentUser User user, @PathVariable Long farmId) {
        favoriteService.addFavorite(user, farmId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@CurrentUser User user) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(user);
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(@RequestParam Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }

}
