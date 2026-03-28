package com.example.garaoto.controller;

import com.example.garaoto.dto.request.NhanVienKyThuatRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.NhanVienKyThuatResponse;
import com.example.garaoto.service.NhanVienKyThuatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhan-vien-ky-thuat")
@RequiredArgsConstructor
public class NhanVienKyThuatController {

    private final NhanVienKyThuatService nhanVienKyThuatService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NhanVienKyThuatResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<NhanVienKyThuatResponse>>builder()
                .success(true)
                .message("Lấy danh sách nhân viên kỹ thuật thành công")
                .data(nhanVienKyThuatService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NhanVienKyThuatResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<NhanVienKyThuatResponse>builder()
                .success(true)
                .message("Lấy thông tin nhân viên kỹ thuật thành công")
                .data(nhanVienKyThuatService.getById(id))
                .build());
    }

    @GetMapping("/nguoi-dung/{maNguoiDung}")
    public ResponseEntity<ApiResponse<NhanVienKyThuatResponse>> getByNguoiDungId(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(ApiResponse.<NhanVienKyThuatResponse>builder()
                .success(true)
                .message("Lấy thông tin nhân viên kỹ thuật thành công")
                .data(nhanVienKyThuatService.getByNguoiDungId(maNguoiDung))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NhanVienKyThuatResponse>> update(@PathVariable Integer id,
                                                                       @Valid @RequestBody NhanVienKyThuatRequest request) {
        return ResponseEntity.ok(ApiResponse.<NhanVienKyThuatResponse>builder()
                .success(true)
                .message("Cập nhật nhân viên kỹ thuật thành công")
                .data(nhanVienKyThuatService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        nhanVienKyThuatService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa nhân viên kỹ thuật thành công")
                .data(null)
                .build());
    }
}