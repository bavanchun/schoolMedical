package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.AuthRequest;
import com.schoolhealth.schoolmedical.model.dto.request.RegisterRequest;
import com.schoolhealth.schoolmedical.model.dto.response.AuthenticationResponse;
import com.schoolhealth.schoolmedical.service.authenticate.AuthenticateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API quản lý authentication như đăng ký, đăng nhập")
public class AuthenticationController {

    private final AuthenticateService authenticateService;

    @Operation(
            summary = "Đăng ký tài khoản mới",
            description = "API để đăng ký tài khoản người dùng mới vào hệ thống và trả về JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng ký thành công", content = {
                    @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc không đủ", content = {
                    @Content(schema = @Schema(implementation = String.class))
            })
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticateService.register(request));
    }

    @Operation(
            summary = "Đăng nhập vào hệ thống",
            description = "API để đăng nhập bằng số điện thoại và mật khẩu, trả về JWT token để sử dụng cho các API yêu cầu xác thực"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công", content = {
                    @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Thông tin đăng nhập không chính xác")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticateService.authenticate(request));
    }
}
