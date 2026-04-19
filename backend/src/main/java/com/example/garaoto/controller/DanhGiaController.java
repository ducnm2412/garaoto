package com.example.garaoto.controller;

import com.example.garaoto.dto.request.DanhGiaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.DanhGiaResponse;
import com.example.garaoto.service.DanhGiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-gia")
@RequiredArgsConstructor
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    @PostMapping
    @PreAuthorize("hasRole('KhachHang')")
    public ResponseEntity<ApiResponse<DanhGiaResponse>> createDanhGia(@Valid @RequestBody DanhGiaRequest request) {
        DanhGiaResponse response = danhGiaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Gửi đánh giá thành công", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DanhGiaResponse>>> getAllDanhGia() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách đánh giá thành công", danhGiaService.getAll()));
    }

    @GetMapping("/khach-hang/{maNguoiDung}")
    public ResponseEntity<ApiResponse<List<DanhGiaResponse>>> getByKhachHang(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy đánh giá của khách hàng thành công", danhGiaService.getByKhachHang(maNguoiDung)));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<DanhGiaResponse>> checkDanhGia(
            @RequestParam String loaiDichVu,
            @RequestParam Integer maThamChieu) {
        DanhGiaResponse res = danhGiaService.getByThuThieu(loaiDichVu, maThamChieu);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ok", res));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ApiResponse<Void>> deleteDanhGia(@PathVariable Integer id) {
        danhGiaService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xóa đánh giá thành công", null));
    }
}
