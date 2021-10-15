package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateResponse;
import com.ezfarm.ezfarmback.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "유저 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @ApiOperation(value = "유저 로그인")
  @PostMapping("/login")
  public void fakeLogin(@RequestBody LoginRequest loginRequest) {
    throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
  }

  @ApiOperation(value = "유저 회원가입")
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
    Long userId = userService.createUser(signUpRequest);
    return ResponseEntity.created(URI.create("/api/user/" + userId)).build();
  }

  @ApiOperation(value = "내 정보 조회")
  @GetMapping
  public ResponseEntity<UserResponse> findUser(@CurrentUser User user) {
    return ResponseEntity.ok(userService.findUser(user));
  }

  @ApiOperation(value = "내 정보 수정")
  @PatchMapping
  public ResponseEntity<UserUpdateResponse> updateUser(@CurrentUser User user,
      @ModelAttribute UserUpdateRequest request) {
    UserUpdateResponse userUpdateResponse = userService.updateUser(user, request);
    return ResponseEntity.ok(userUpdateResponse);
  }

  @ApiOperation(value = "유저 회원탈퇴")
  @DeleteMapping
  public ResponseEntity<Void> deleteUser(@CurrentUser User user) {
    userService.deleteUser(user);
    return ResponseEntity.noContent().build();
  }
}
