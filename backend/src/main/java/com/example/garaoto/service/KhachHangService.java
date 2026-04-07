package com.example.garaoto.service;

import com.example.garaoto.dto.request.KhachHangRequest;
import com.example.garaoto.dto.response.KhachHangResponse;

import java.util.List;

public interface KhachHangService {
    List<KhachHangResponse> getAll();
    KhachHangResponse getById(Integer id);
    KhachHangResponse getByNguoiDungId(Integer maNguoiDung);
    KhachHangResponse update(Integer id, KhachHangRequest request);
    void delete(Integer id);
}