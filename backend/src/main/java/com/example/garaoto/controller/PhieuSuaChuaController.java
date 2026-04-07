package com.example.garaoto.controller;

import com.example.garaoto.dto.request.PhieuSuaChuaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.PhieuSuaChuaResponse;
import com.example.garaoto.service.PhieuSuaChuaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-sua-chua")
@RequiredArgsConstructor
public class PhieuSuaChuaController {

    private final PhieuSuaChuaService phieuSuaChuaService;

    @PostMapping
    public ResponseEntity<ApiResponse<PhieuSuaChuaResponse>> create(@Valid @RequestBody PhieuSuaChuaRequest request) {
        return ResponseEntity.ok(ApiResponse.<PhieuSuaChuaResponse>builder()
                .success(true)
                .message("Tạo phiếu sửa chữa thành công")
                .data(phieuSuaChuaService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PhieuSuaChuaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<PhieuSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy danh sách phiếu sửa chữa thành công")
                .data(phieuSuaChuaService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PhieuSuaChuaResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<PhieuSuaChuaResponse>builder()
                .success(true)
                .message("Lấy thông tin phiếu sửa chữa thành công")
                .data(phieuSuaChuaService.getById(id))
                .build());
    }

    @GetMapping("/khach-hang/{maKhachHang}")
    public ResponseEntity<ApiResponse<List<PhieuSuaChuaResponse>>> getByKhachHang(@PathVariable Integer maKhachHang) {
        return ResponseEntity.ok(ApiResponse.<List<PhieuSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy phiếu sửa theo khách hàng thành công")
                .data(phieuSuaChuaService.getByKhachHang(maKhachHang))
                .build());
    }

    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<ApiResponse<PhieuSuaChuaResponse>> updateStatus(@PathVariable Integer id,
                                                                          @RequestParam String trangThai) {
        return ResponseEntity.ok(ApiResponse.<PhieuSuaChuaResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái phiếu sửa chữa thành công")
                .data(phieuSuaChuaService.updateStatus(id, trangThai))
                .build());
    }

    @PatchMapping("/{id}/chan-doan")
    public ResponseEntity<ApiResponse<PhieuSuaChuaResponse>> updateChanDoan(@PathVariable Integer id,
                                                                            @RequestParam String chanDoan,
                                                                            @RequestParam(required = false) String trangThai) {
        return ResponseEntity.ok(ApiResponse.<PhieuSuaChuaResponse>builder()
                .success(true)
                .message("Cập nhật chẩn đoán phiếu sửa chữa thành công")
                .data(phieuSuaChuaService.updateChanDoan(id, chanDoan, trangThai))
                .build());
    }
}