package com.example.garaoto.controller;

import com.example.garaoto.dto.request.ThanhToanRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.ThanhToanResponse;
import com.example.garaoto.service.ThanhToanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thanh-toan")
@RequiredArgsConstructor
public class ThanhToanController {

    private final ThanhToanService thanhToanService;

    @PostMapping
    public ResponseEntity<ApiResponse<ThanhToanResponse>> create(@Valid @RequestBody ThanhToanRequest request) {
        return ResponseEntity.ok(ApiResponse.<ThanhToanResponse>builder()
                .success(true)
                .message("Tạo thanh toán thành công")
                .data(thanhToanService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ThanhToanResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<ThanhToanResponse>>builder()
                .success(true)
                .message("Lấy danh sách thanh toán thành công")
                .data(thanhToanService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ThanhToanResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<ThanhToanResponse>builder()
                .success(true)
                .message("Lấy thông tin thanh toán thành công")
                .data(thanhToanService.getById(id))
                .build());
    }

    @GetMapping("/khach-hang/{maKhachHang}")
    public ResponseEntity<ApiResponse<List<ThanhToanResponse>>> getByKhachHang(@PathVariable Integer maKhachHang) {
        return ResponseEntity.ok(ApiResponse.<List<ThanhToanResponse>>builder()
                .success(true)
                .message("Lấy thanh toán theo khách hàng thành công")
                .data(thanhToanService.getByKhachHang(maKhachHang))
                .build());
    }
}