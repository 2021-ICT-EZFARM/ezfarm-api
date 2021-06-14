package com.ezfarm.ezfarmback.user.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
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

    public Long createUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
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

    public void updateUser(User user, UserUpdateRequest userUpdateRequest) {
        User findUser = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.NON_EXISTENT_USER));
        findUser.updateUser(userUpdateRequest);
    }
}
