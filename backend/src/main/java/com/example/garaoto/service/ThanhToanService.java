package com.example.garaoto.service;

import com.example.garaoto.dto.request.ThanhToanRequest;
import com.example.garaoto.dto.response.ThanhToanResponse;

import java.util.List;

public interface ThanhToanService {
    ThanhToanResponse create(ThanhToanRequest request);
    List<ThanhToanResponse> getAll();
    ThanhToanResponse getById(Integer id);
    List<ThanhToanResponse> getByKhachHang(Integer maNguoiDung);
}
