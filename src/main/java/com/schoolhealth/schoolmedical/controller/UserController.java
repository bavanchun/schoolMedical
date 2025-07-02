package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService; // Assuming UserService is defined elsewhere
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
