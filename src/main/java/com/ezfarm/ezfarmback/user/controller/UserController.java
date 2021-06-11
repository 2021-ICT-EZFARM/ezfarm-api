package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 접근 테스트를 위한 컨트롤러입니다.
    @GetMapping("/access-test")
    public ResponseEntity<UserResponse> accessTest(@CurrentUser User user) {
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long userId = userService.registerUser(signUpRequest);
        return ResponseEntity.created(URI.create("/api/user/" + userId)).build();
    }

}
