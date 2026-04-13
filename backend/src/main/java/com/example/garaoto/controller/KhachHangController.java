package com.example.garaoto.controller;

import com.example.garaoto.dto.request.KhachHangRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.KhachHangResponse;
import com.example.garaoto.service.KhachHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@RequiredArgsConstructor
public class KhachHangController {

    private final KhachHangService khachHangService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<KhachHangResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<KhachHangResponse>>builder()
                .success(true)
                .message("Lấy danh sách khách hàng thành công")
                .data(khachHangService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHangResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<KhachHangResponse>builder()
                .success(true)
                .message("Lấy thông tin khách hàng thành công")
                .data(khachHangService.getById(id))
                .build());
    }

    @GetMapping("/nguoi-dung/{maNguoiDung}")
    public ResponseEntity<ApiResponse<KhachHangResponse>> getByNguoiDungId(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(ApiResponse.<KhachHangResponse>builder()
                .success(true)
                .message("Lấy thông tin khách hàng thành công")
                .data(khachHangService.getByNguoiDungId(maNguoiDung))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHangResponse>> update(@PathVariable Integer id,
                                                                 @Valid @RequestBody KhachHangRequest request) {
        return ResponseEntity.ok(ApiResponse.<KhachHangResponse>builder()
                .success(true)
                .message("Cập nhật khách hàng thành công")
                .data(khachHangService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        khachHangService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa khách hàng thành công")
                .data(null)
                .build());
    }
}
