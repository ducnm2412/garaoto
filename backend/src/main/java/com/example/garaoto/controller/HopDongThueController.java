package com.example.garaoto.controller;

import com.example.garaoto.dto.request.HopDongThueRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.HopDongThueResponse;
import com.example.garaoto.service.HopDongThueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hop-dong-thue")
@RequiredArgsConstructor
public class HopDongThueController {

    private final HopDongThueService hopDongThueService;

    @PostMapping
    public ResponseEntity<ApiResponse<HopDongThueResponse>> create(@Valid @RequestBody HopDongThueRequest request) {
        return ResponseEntity.ok(ApiResponse.<HopDongThueResponse>builder()
                .success(true)
                .message("Tạo hợp đồng thuê thành công")
                .data(hopDongThueService.create(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HopDongThueResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<HopDongThueResponse>>builder()
                .success(true)
                .message("Lấy danh sách hợp đồng thuê thành công")
                .data(hopDongThueService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HopDongThueResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<HopDongThueResponse>builder()
                .success(true)
                .message("Lấy thông tin hợp đồng thuê thành công")
                .data(hopDongThueService.getById(id))
                .build());
    }

    @GetMapping("/don-thue/{maDonThue}")
    public ResponseEntity<ApiResponse<HopDongThueResponse>> getByDonThue(@PathVariable Integer maDonThue) {
        return ResponseEntity.ok(ApiResponse.<HopDongThueResponse>builder()
                .success(true)
                .message("Lấy hợp đồng thuê theo đơn thuê thành công")
                .data(hopDongThueService.getByDonThue(maDonThue))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        hopDongThueService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa hợp đồng thuê thành công")
                .data(null)
                .build());
    }
}