package com.example.garaoto.controller;

import com.example.garaoto.dto.request.LoginRequest;
import com.example.garaoto.dto.request.RegisterRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.AuthResponse;
import com.example.garaoto.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        if (request.getVaiTro() != null && !request.getVaiTro().equalsIgnoreCase("KhachHang")) {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {
                return ResponseEntity.status(403).body(ApiResponse.<AuthResponse>builder()
                        .success(false)
                        .message("Chỉ Admin mới có quyền tạo tài khoản nhân sự")
                        .data(null).build());
            }
        }
        
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Đăng ký thành công")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Đăng nhập thành công")
                        .data(response)
                        .build()
        );
    }
}
