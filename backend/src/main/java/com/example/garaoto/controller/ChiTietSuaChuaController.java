package com.example.garaoto.controller;

import com.example.garaoto.dto.request.ChiTietSuaChuaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.ChiTietSuaChuaResponse;
import com.example.garaoto.service.ChiTietSuaChuaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chi-tiet-sua-chua")
@RequiredArgsConstructor
public class ChiTietSuaChuaController {

    private final ChiTietSuaChuaService chiTietSuaChuaService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChiTietSuaChuaResponse>> create(@Valid @RequestBody ChiTietSuaChuaRequest request) {
        return ResponseEntity.ok(ApiResponse.<ChiTietSuaChuaResponse>builder()
                .success(true)
                .message("Tạo chi tiết sửa chữa thành công")
                .data(chiTietSuaChuaService.create(request))
                .build());
    }

    @GetMapping("/phieu-sua/{maPhieuSua}")
    public ResponseEntity<ApiResponse<List<ChiTietSuaChuaResponse>>> getByPhieuSua(@PathVariable Integer maPhieuSua) {
        return ResponseEntity.ok(ApiResponse.<List<ChiTietSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy chi tiết sửa chữa theo phiếu sửa thành công")
                .data(chiTietSuaChuaService.getByPhieuSua(maPhieuSua))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        chiTietSuaChuaService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa chi tiết sửa chữa thành công")
                .data(null)
                .build());
    }
}
