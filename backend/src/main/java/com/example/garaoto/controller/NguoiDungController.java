package com.example.garaoto.controller;

import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.NguoiDungResponse;
import com.example.garaoto.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi-dung")
@RequiredArgsConstructor
public class NguoiDungController {

    private final NguoiDungService nguoiDungService;

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<List<NguoiDungResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<NguoiDungResponse>>builder()
                .success(true)
                .message("Lấy danh sách người dùng thành công")
                .data(nguoiDungService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<NguoiDungResponse>builder()
                .success(true)
                .message("Lấy thông tin người dùng thành công")
                .data(nguoiDungService.getById(id))
                .build());
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.<NguoiDungResponse>builder()
                .success(true)
                .message("Lấy thông tin người dùng thành công")
                .data(nguoiDungService.getByEmail(email))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NguoiDungResponse>> update(@PathVariable Integer id,
                                                                 @RequestBody com.example.garaoto.dto.request.NguoiDungRequest request) {
        return ResponseEntity.ok(ApiResponse.<NguoiDungResponse>builder()
                .success(true)
                .message("Cập nhật thông tin người dùng thành công")
                .data(nguoiDungService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        nguoiDungService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa người dùng thành công")
                .data(null)
                .build());
    }
}
