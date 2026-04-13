package com.example.garaoto.controller;

import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/names")
    public ResponseEntity<ApiResponse<List<String>>> getAllAdminNames() {
        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
                .success(true)
                .message("Lấy danh sách admin thành công")
                .data(adminService.getAllAdminNames())
                .build());
    }
}
