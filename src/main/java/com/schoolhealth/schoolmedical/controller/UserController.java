package com.schoolhealth.schoolmedical.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
}
