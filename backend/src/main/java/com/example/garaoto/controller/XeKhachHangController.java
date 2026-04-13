package com.example.garaoto.controller;

import com.example.garaoto.dto.request.XeKhachHangRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.XeKhachHangResponse;
import com.example.garaoto.service.XeKhachHangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xe-khach-hang")
@RequiredArgsConstructor
public class XeKhachHangController {

    private final XeKhachHangService xeKhachHangService;

    @PostMapping
    public ResponseEntity<ApiResponse<XeKhachHangResponse>> create(@Valid @RequestBody XeKhachHangRequest request) {
        return ResponseEntity.ok(ApiResponse.<XeKhachHangResponse>builder()
                .success(true)
                .message("Tạo xe khách hàng thành công")
                .data(xeKhachHangService.create(request))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<XeKhachHangResponse>> update(@PathVariable Integer id,
                                                                   @Valid @RequestBody XeKhachHangRequest request) {
        return ResponseEntity.ok(ApiResponse.<XeKhachHangResponse>builder()
                .success(true)
                .message("Cập nhật xe khách hàng thành công")
                .data(xeKhachHangService.update(id, request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<XeKhachHangResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<XeKhachHangResponse>>builder()
                .success(true)
                .message("Lấy danh sách xe khách hàng thành công")
                .data(xeKhachHangService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<XeKhachHangResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<XeKhachHangResponse>builder()
                .success(true)
                .message("Lấy thông tin xe khách hàng thành công")
                .data(xeKhachHangService.getById(id))
                .build());
    }

    @GetMapping("/khach-hang/{maNguoiDung}")
    public ResponseEntity<ApiResponse<List<XeKhachHangResponse>>> getByKhachHang(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(ApiResponse.<List<XeKhachHangResponse>>builder()
                .success(true)
                .message("Lấy danh sách xe theo khách hàng thành công")
                .data(xeKhachHangService.getByKhachHang(maNguoiDung))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        xeKhachHangService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa xe khách hàng thành công")
                .data(null)
                .build());
    }
}
