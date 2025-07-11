package com.schoolhealth.schoolmedical.service.user;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.request.ChangePasswordRequest;
import com.schoolhealth.schoolmedical.model.dto.request.UserDeviceToken;
import com.schoolhealth.schoolmedical.model.dto.request.UserRequest;
import com.schoolhealth.schoolmedical.model.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse getUserById(String userId);
    List<UserResponse> getAllUsers();

    UserResponse updateUserById(String userId, UserRequest userRequest);
    List<User> findAllByRole(Role role);
    boolean updateDeviceToken(String userId, String deviceToken);
    String getCurrentUserId(HttpServletRequest request);

    List<User> findAllWithPupilByParent();
    User findById(String userId);

    boolean changePassword(String userId, ChangePasswordRequest request);

}
