package com.ezfarm.ezfarmback.favorite.controller;

import com.ezfarm.ezfarmback.favorite.dto.FavoriteRequest;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.favorite.service.FavoriteService;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @PostMapping
  public ResponseEntity<Void> addFavorite(@CurrentUser User user,
      @RequestBody @Valid FavoriteRequest request) {
    favoriteService.addFavorite(user, request);
    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "즐겨찾기 조회")
  @GetMapping
  public ResponseEntity<List<FavoriteResponse>> findFavorites(@CurrentUser User user) {
    List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(user);
    return ResponseEntity.ok(favoriteResponses);
  }

  @ApiOperation(value = "즐겨찾기 삭제")
  @DeleteMapping
  public ResponseEntity<Void> deleteFavorite(@RequestParam Long favoriteId) {
    favoriteService.deleteFavorite(favoriteId);
    return ResponseEntity.noContent().build();
  }
}