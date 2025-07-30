package com.schoolhealth.schoolmedical.service.user;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.ChangePasswordRequest;
import com.schoolhealth.schoolmedical.model.dto.request.UserDeviceToken;
import com.schoolhealth.schoolmedical.model.dto.request.UserRequest;
import com.schoolhealth.schoolmedical.model.dto.response.TotalUser;
import com.schoolhealth.schoolmedical.model.dto.response.TotalUserRole;
import com.schoolhealth.schoolmedical.model.dto.response.UserResponse;
import com.schoolhealth.schoolmedical.model.mapper.UserMapper;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEndcoder;

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return userMapper.toUserResponse(user);
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

    @Override
    public List<User> findAllWithPupilByParent() {
        return userRepository.findAllWithPupilsByParent();
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    @Override
    public boolean changePassword(String userId, ChangePasswordRequest request) {
        // Validate the request
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // find user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        // Verify current password
        if (!passwordEndcoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw  new IllegalArgumentException("Current password do not match");
        }

        // check if new password is same as current password
        if (passwordEndcoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as current password");
        }

        // encode and update password
        user.setPassword(passwordEndcoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return true;
    }



    @Override
    public TotalUser getTotalUsersGroupedByRole() {
        List<TotalUserRole> totalUserRoles = userRepository.countTotalUserByRole();
        if( totalUserRoles != null && !totalUserRoles.isEmpty()) {
            long total = totalUserRoles.stream().mapToLong(TotalUserRole::getTotalUser).sum();
            return TotalUser.builder()
                    .total(total)
                    .totalUserRoles(totalUserRoles)
                    .build();
        }
        throw new NotFoundException("No user data found to group by role");
    }

    @Override
    public void updateRoleForUser(String userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

}
