package com.example.garaoto.controller;

import com.example.garaoto.dto.request.XeChoThueRequest;
import com.example.garaoto.dto.response.ApiResponse;
import com.example.garaoto.dto.response.XeChoThueResponse;
import com.example.garaoto.service.XeChoThueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xe-cho-thue")
@RequiredArgsConstructor
public class XeChoThueController {

    private final XeChoThueService xeChoThueService;

    @PostMapping
    public ResponseEntity<ApiResponse<XeChoThueResponse>> create(@Valid @RequestBody XeChoThueRequest request) {
        return ResponseEntity.ok(ApiResponse.<XeChoThueResponse>builder()
                .success(true)
                .message("Tạo xe cho thuê thành công")
                .data(xeChoThueService.create(request))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<XeChoThueResponse>> update(@PathVariable Integer id,
                                                                 @Valid @RequestBody XeChoThueRequest request) {
        return ResponseEntity.ok(ApiResponse.<XeChoThueResponse>builder()
                .success(true)
                .message("Cập nhật xe cho thuê thành công")
                .data(xeChoThueService.update(id, request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<XeChoThueResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<XeChoThueResponse>>builder()
                .success(true)
                .message("Lấy danh sách xe cho thuê thành công")
                .data(xeChoThueService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<XeChoThueResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<XeChoThueResponse>builder()
                .success(true)
                .message("Lấy thông tin xe cho thuê thành công")
                .data(xeChoThueService.getById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        xeChoThueService.delete(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Xóa xe cho thuê thành công")
                .data(null)
                .build());
    }
}
