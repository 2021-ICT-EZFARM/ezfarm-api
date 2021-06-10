package com.ezfarm.ezfarmback.user.service;

import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Long registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        User result = userRepository.save(user);
        return result.getId();
    }
}
