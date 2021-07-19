package com.ezfarm.ezfarmback.user.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.utils.fileupload.FileStoreService;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final FileStoreService fileStoreService;

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

    public UserUpdateResponse updateUser(User user, UserUpdateRequest userUpdateRequest) {
        User findUser = userRepository.findByEmail(user.getEmail()).orElseThrow(
            () -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        String imageUrl = getStoreImageUrl(findUser, userUpdateRequest.getImage(),
            userUpdateRequest.isDefaultImage());

        findUser.updateUser(userUpdateRequest, imageUrl);
        return UserUpdateResponse.of(findUser);
    }

    private String getStoreImageUrl(User user, MultipartFile imageFile, boolean isDefaultImage) {
        if (isNotUpdateImage(imageFile, isDefaultImage)) {
            return user.getImageUrl();
        }
        if (isUpdateImage(imageFile, isDefaultImage)) {
            return getUpdatedImageUrl(user, imageFile);
        }
        if (isUpdateDefaultImage(imageFile, isDefaultImage)) {
            deleteOriginImage(user);
        }
        return null;
    }

    private boolean isNotUpdateImage(MultipartFile newImageFile, boolean isDefaultImage) {
        return newImageFile == null && !isDefaultImage;
    }

    private boolean isUpdateImage(MultipartFile newImageFile, boolean isDefaultImage) {
        return newImageFile != null && !isDefaultImage;
    }

    private boolean isUpdateDefaultImage(MultipartFile newImageFile, boolean isDefaultImage) {
        return newImageFile == null && isDefaultImage;
    }

    private String getUpdatedImageUrl(User user, MultipartFile newImageFile) {
        deleteOriginImage(user);
        return fileStoreService.storeFile(newImageFile);
    }

    private void deleteOriginImage(User user) {
        if (user.hasImage()) {
            fileStoreService.deleteFile(user.getImageUrl());
        }
    }
}
