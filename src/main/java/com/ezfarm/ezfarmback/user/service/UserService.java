package com.ezfarm.ezfarmback.user.service;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.upload.FileUploader;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest.IsDefaultImage;
import com.ezfarm.ezfarmback.user.dto.UserUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserRepository userRepository;

  private final FileUploader fileUploader;

  private final PasswordEncoder passwordEncoder;

  private final ModelMapper modelMapper;

  public Long createUser(SignUpRequest signUpRequest) {
    validateIsDuplicatedEmail(signUpRequest.getEmail());
    User user = User.create(signUpRequest, passwordEncoder.encode(signUpRequest.getPassword()));
    return userRepository.save(user).getId();
  }

  public void validateIsDuplicatedEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
    }
  }

  @Transactional(readOnly = true)
  public UserResponse findUser(User user) {
    User findUser = validateUserIdAndGetUser(user);
    return modelMapper.map(findUser, UserResponse.class);
  }

  public UserUpdateResponse updateUser(User user, UserUpdateRequest request) {
    User findUser = validateUserIdAndGetUser(user);
    findUser.update(request);
    uploadImageUrl(findUser, request.getImage(), request.getIsDefaultImage());
    return UserUpdateResponse.of(findUser);
  }

  public User validateUserIdAndGetUser(User loginUser) {
    return userRepository.findById(loginUser.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));
  }

  private void uploadImageUrl(User user, MultipartFile image, IsDefaultImage isDefaultImage) {
    deleteImage(user);
    if (isUpdateImage(image, isDefaultImage)) {
      user.updateImageUrl(uploadImageAndGetImageUrl(image));
    } else if (isUpdateDefaultImage(image, isDefaultImage)) {
      user.deleteImageUrl();
    }
  }

  private void deleteImage(User user) {
    if (user.hasImage()) {
      fileUploader.deleteFile(user.getImageUrl());
    }
  }

  private boolean isUpdateImage(MultipartFile image, IsDefaultImage isDefault) {
    return image != null && isDefault == IsDefaultImage.N;
  }

  private boolean isUpdateDefaultImage(MultipartFile image, IsDefaultImage isDefault) {
    return image == null && isDefault == IsDefaultImage.Y;
  }

  private String uploadImageAndGetImageUrl(MultipartFile image) {
    return fileUploader.storeFile(image);
  }

  public void deleteUser(User user) {
    userRepository.deleteById(user.getId());
  }
}
