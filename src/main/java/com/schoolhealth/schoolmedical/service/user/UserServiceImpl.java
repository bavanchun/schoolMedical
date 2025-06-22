package com.schoolhealth.schoolmedical.service.user;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.request.UserDeviceToken;
import com.schoolhealth.schoolmedical.model.dto.request.UserRequest;
import com.schoolhealth.schoolmedical.model.dto.response.UserResponse;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public UserResponse getUserById(String userId) {
        return null;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public UserResponse updateUserById(String userId, UserRequest userRequest) {
        return null;
    }
    @Override
    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public boolean updateDeviceToken(String userId, String deviceToken) {
        int result = userRepository.updateDeviceToken(userId, deviceToken) ;
        return result > 0;
    }

    @Override
    public String getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUserId(token);
        }
        return null;
    }

}
