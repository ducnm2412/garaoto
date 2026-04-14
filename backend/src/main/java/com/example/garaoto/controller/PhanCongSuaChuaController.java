package com.example.garaoto.controller;

import com.example.garaoto.dto.request.PhanCongSuaChuaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.PhanCongSuaChuaResponse;
import com.example.garaoto.service.PhanCongSuaChuaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phan-cong-sua-chua")
@RequiredArgsConstructor
public class PhanCongSuaChuaController {

    private final PhanCongSuaChuaService phanCongSuaChuaService;

    @PostMapping
    public ResponseEntity<ApiResponse<PhanCongSuaChuaResponse>> create(@Valid @RequestBody PhanCongSuaChuaRequest request) {
        return ResponseEntity.ok(ApiResponse.<PhanCongSuaChuaResponse>builder()
                .success(true)
                .message("Phân công sửa chữa thành công")
                .data(phanCongSuaChuaService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PhanCongSuaChuaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<PhanCongSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy danh sách phân công sửa chữa thành công")
                .data(phanCongSuaChuaService.getAll())
                .build());
    }

    @GetMapping("/nhan-vien/{maNguoiDung}")
    public ResponseEntity<ApiResponse<List<PhanCongSuaChuaResponse>>> getByNhanVien(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(ApiResponse.<List<PhanCongSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy danh sách phân công theo nhân viên thành công")
                .data(phanCongSuaChuaService.getByNhanVien(maNguoiDung))
                .build());
    }

    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<ApiResponse<PhanCongSuaChuaResponse>> updateStatus(@PathVariable Integer id,
                                                                             @RequestParam String trangThai) {
        return ResponseEntity.ok(ApiResponse.<PhanCongSuaChuaResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái phân công thành công")
                .data(phanCongSuaChuaService.updateStatus(id, trangThai))
                .build());
    }
}
