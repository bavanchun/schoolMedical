package com.schoolhealth.schoolmedical.service.user;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.model.dto.request.UserRequest;
import com.schoolhealth.schoolmedical.model.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse getUserById(String userId);
    List<UserResponse> getAllUsers();

    UserResponse updateUserById(String userId, UserRequest userRequest);
}
