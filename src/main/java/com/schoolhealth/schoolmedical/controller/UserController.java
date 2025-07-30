package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.ChangePasswordRequest;
import com.schoolhealth.schoolmedical.model.dto.response.TotalUser;
import com.schoolhealth.schoolmedical.model.dto.response.UserResponse;
import com.schoolhealth.schoolmedical.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "API quản lý thông tin người dùng")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(
            summary = "Lấy thông tin người dùng hiện tại",
            description = "API để lấy thông tin chi tiết của người dùng hiện tại dựa trên JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công", content = {
                    @Content(schema = @Schema(implementation = UserResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Không có quyền truy cập hoặc token không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy thông tin người dùng")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUserInfo(HttpServletRequest request) {
        String currentUserId = userService.getCurrentUserId(request);
        if (currentUserId == null) {
            return ResponseEntity.status(401).build();
        }
        UserResponse userResponse = userService.getUserById(currentUserId);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        try {
            String currentUserId = userService.getCurrentUserId(httpRequest);
            if (currentUserId == null) {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            boolean success = userService.changePassword(currentUserId, request);
            if (success) {
                return ResponseEntity.ok("Password changed successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to change password");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while changing password");
        }
    }

    @PatchMapping("/token/{userId}")
    public ResponseEntity<?> saveDeviceToken(@PathVariable String userId, @RequestBody Map<String, String> deviceToken) {
        // Logic to save device token
        System.out.println("userId = " + userId + ", deviceToken = " + deviceToken);
        String token = deviceToken.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Token is required");
        }
        boolean isUpdated = userService.updateDeviceToken(userId, token);
        return isUpdated
            ? ResponseEntity.ok("Device token updated successfully")
            : ResponseEntity.status(500).body("Failed to update device token");
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get total number of users",
            description = "API to retrieve the total number of users in the system"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user count", content = {
                    @Content(schema = @Schema(implementation = TotalUser.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error occurred while retrieving user count")
    })
    public ResponseEntity<?> getUserCount() {
        return ResponseEntity.ok(userService.getTotalUsersGroupedByRole());
    }
}
