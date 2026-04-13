package com.example.garaoto.controller;

import com.example.garaoto.dto.request.DichVuSuaChuaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.DichVuSuaChuaResponse;
import com.example.garaoto.service.DichVuSuaChuaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dich-vu-sua-chua")
@RequiredArgsConstructor
public class DichVuSuaChuaController {

    private final DichVuSuaChuaService dichVuSuaChuaService;

    @PostMapping
    public ResponseEntity<ApiResponse<DichVuSuaChuaResponse>> create(@Valid @RequestBody DichVuSuaChuaRequest request) {
        return ResponseEntity.ok(ApiResponse.<DichVuSuaChuaResponse>builder()
                .success(true)
                .message("Tạo dịch vụ sửa chữa thành công")
                .data(dichVuSuaChuaService.create(request))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DichVuSuaChuaResponse>> update(@PathVariable Integer id,
                                                                     @Valid @RequestBody DichVuSuaChuaRequest request) {
        return ResponseEntity.ok(ApiResponse.<DichVuSuaChuaResponse>builder()
                .success(true)
                .message("Cập nhật dịch vụ sửa chữa thành công")
                .data(dichVuSuaChuaService.update(id, request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DichVuSuaChuaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<DichVuSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy danh sách dịch vụ sửa chữa thành công")
                .data(dichVuSuaChuaService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DichVuSuaChuaResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<DichVuSuaChuaResponse>builder()
                .success(true)
                .message("Lấy thông tin dịch vụ sửa chữa thành công")
                .data(dichVuSuaChuaService.getById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        dichVuSuaChuaService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa dịch vụ sửa chữa thành công")
                .data(null)
                .build());
    }
}
