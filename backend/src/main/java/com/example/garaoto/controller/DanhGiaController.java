package com.example.garaoto.controller;

import com.example.garaoto.dto.request.DanhGiaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.DanhGiaResponse;
import com.example.garaoto.service.DanhGiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-gia")
@RequiredArgsConstructor
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    @PostMapping
    public ResponseEntity<ApiResponse<DanhGiaResponse>> create(@Valid @RequestBody DanhGiaRequest request) {
        return ResponseEntity.ok(ApiResponse.<DanhGiaResponse>builder()
                .success(true)
                .message("Tạo đánh giá thành công")
                .data(danhGiaService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DanhGiaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<DanhGiaResponse>>builder()
                .success(true)
                .message("Lấy danh sách đánh giá thành công")
                .data(danhGiaService.getAll())
                .build());
    }

    @GetMapping("/khach-hang/{maKhachHang}")
    public ResponseEntity<ApiResponse<List<DanhGiaResponse>>> getByKhachHang(@PathVariable Integer maKhachHang) {
        return ResponseEntity.ok(ApiResponse.<List<DanhGiaResponse>>builder()
                .success(true)
                .message("Lấy đánh giá theo khách hàng thành công")
                .data(danhGiaService.getByKhachHang(maKhachHang))
                .build());
    }
}