package com.example.garaoto.controller;

import com.example.garaoto.dto.request.LichHenSuaChuaRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.LichHenSuaChuaResponse;
import com.example.garaoto.service.LichHenSuaChuaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lich-hen-sua-chua")
@RequiredArgsConstructor
public class LichHenSuaChuaController {

    private final LichHenSuaChuaService lichHenSuaChuaService;

    @PostMapping
    public ResponseEntity<ApiResponse<LichHenSuaChuaResponse>> create(@Valid @RequestBody LichHenSuaChuaRequest request) {
        return ResponseEntity.ok(ApiResponse.<LichHenSuaChuaResponse>builder()
                .success(true)
                .message("Tạo lịch hẹn sửa chữa thành công")
                .data(lichHenSuaChuaService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LichHenSuaChuaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<LichHenSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy danh sách lịch hẹn sửa chữa thành công")
                .data(lichHenSuaChuaService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LichHenSuaChuaResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<LichHenSuaChuaResponse>builder()
                .success(true)
                .message("Lấy thông tin lịch hẹn sửa chữa thành công")
                .data(lichHenSuaChuaService.getById(id))
                .build());
    }

    @GetMapping("/khach-hang/{MaNguoiDung}")
    public ResponseEntity<ApiResponse<List<LichHenSuaChuaResponse>>> getByKhachHang(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(ApiResponse.<List<LichHenSuaChuaResponse>>builder()
                .success(true)
                .message("Lấy lịch hẹn theo khách hàng thành công")
                .data(lichHenSuaChuaService.getByKhachHang(maNguoiDung))
                .build());
    }

    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<ApiResponse<LichHenSuaChuaResponse>> updateStatus(@PathVariable Integer id,
                                                                            @RequestParam String trangThai) {
        return ResponseEntity.ok(ApiResponse.<LichHenSuaChuaResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái lịch hẹn thành công")
                .data(lichHenSuaChuaService.updateStatus(id, trangThai))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        lichHenSuaChuaService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa lịch hẹn sửa chữa thành công")
                .data(null)
                .build());
    }
}
