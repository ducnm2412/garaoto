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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        nguoiDungService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa người dùng thành công")
                .data(null)
                .build());
    }
}