package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateResponse;
import com.ezfarm.ezfarmback.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @ApiOperation(value = "접근 테스트")
    @GetMapping("/access-test")
    public ResponseEntity<UserResponse> accessTest(@CurrentUser User user) {
        return ResponseEntity.ok(modelMapper.map(user, UserResponse.class));
    }

    @ApiOperation(value = "유저 로그인")
    @PostMapping("/login")
    public void fakeLogin(@RequestBody LoginRequest loginRequest) {
        throw new CustomException(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ApiOperation(value = "유저 회원가입")
    @ApiResponse(code = 400, message = "이미 존재하는 이메일입니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long userId = userService.createUser(signUpRequest);
        return ResponseEntity.created(URI.create("/api/user/" + userId)).build();
    }

    @ApiOperation(value = "내 정보 조회")
    @ApiResponse(code = 404, message = "존재하지 않는 사용자 입니다.")
    @GetMapping
    public ResponseEntity<UserResponse> readUser(@CurrentUser User user) {
        User findUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));
        return ResponseEntity.ok(modelMapper.map(findUser, UserResponse.class));
    }

    @ApiOperation(value = "내 정보 수정")
    @ApiResponse(code = 404, message = "존재하지 않는 사용자 입니다.")
    @PatchMapping
    public ResponseEntity<UserUpdateResponse> updateUser(@CurrentUser User user,
        @RequestBody UserUpdateRequest userUpdateRequest) {
        UserUpdateResponse userUpdateResponse = userService.updateUser(user, userUpdateRequest);
        return ResponseEntity.ok(userUpdateResponse);
    }

    @ApiOperation(value = "유저 회원탈퇴")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@CurrentUser User user) {
        userRepository.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

}
