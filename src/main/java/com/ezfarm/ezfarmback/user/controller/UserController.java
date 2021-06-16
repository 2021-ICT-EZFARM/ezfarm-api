package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.security.CurrentUser;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
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

    private final UserRepository userRepository;

    @GetMapping("/access-test")
    public ResponseEntity<UserResponse> accessTest(@CurrentUser User user) {
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long userId = userService.createUser(signUpRequest);
        return ResponseEntity.created(URI.create("/api/user/" + userId)).build();
    }

    @GetMapping
    public ResponseEntity<UserResponse> readUser(@CurrentUser User user) {
        User findUser = userRepository.findByEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        return ResponseEntity.ok(UserResponse.of(findUser));
    }

    @PatchMapping
    public ResponseEntity<Void> updateUser(@CurrentUser User user, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(user, userUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@CurrentUser User user) {
        userRepository.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }

}
