package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        Long userId = userService.registerUser(signUpRequest);
        return ResponseEntity.created(URI.create("/api/user/" + userId)).build();
    }

}
