package com.example.garaoto.controller;

import com.example.garaoto.dto.request.DonThueXeRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.DonThueXeResponse;
import com.example.garaoto.service.DonThueXeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/don-thue-xe")
@RequiredArgsConstructor
public class DonThueXeController {

    private final DonThueXeService donThueXeService;

    @PostMapping
    public ResponseEntity<ApiResponse<DonThueXeResponse>> create(@Valid @RequestBody DonThueXeRequest request) {
        return ResponseEntity.ok(ApiResponse.<DonThueXeResponse>builder()
                .success(true)
                .message("Tạo đơn thuê xe thành công")
                .data(donThueXeService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DonThueXeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<DonThueXeResponse>>builder()
                .success(true)
                .message("Lấy danh sách đơn thuê xe thành công")
                .data(donThueXeService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonThueXeResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<DonThueXeResponse>builder()
                .success(true)
                .message("Lấy thông tin đơn thuê xe thành công")
                .data(donThueXeService.getById(id))
                .build());
    }

    @GetMapping("/khach-hang/{maNguoiDung}")
    public ResponseEntity<ApiResponse<List<DonThueXeResponse>>> getByKhachHang(@PathVariable Integer maNguoiDung) {
        return ResponseEntity.ok(ApiResponse.<List<DonThueXeResponse>>builder()
                .success(true)
                .message("Lấy đơn thuê xe theo khách hàng thành công")
                .data(donThueXeService.getByKhachHang(maNguoiDung))
                .build());
    }

    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<ApiResponse<DonThueXeResponse>> updateStatus(@PathVariable Integer id,
                                                                       @RequestParam String trangThai) {
        return ResponseEntity.ok(ApiResponse.<DonThueXeResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái đơn thuê xe thành công")
                .data(donThueXeService.updateStatus(id, trangThai))
                .build());
    }
}
