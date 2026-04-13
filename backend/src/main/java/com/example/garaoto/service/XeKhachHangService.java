package com.example.garaoto.service;

import com.example.garaoto.dto.request.XeKhachHangRequest;
import com.example.garaoto.dto.response.XeKhachHangResponse;

import java.util.List;

public interface XeKhachHangService {
    XeKhachHangResponse create(XeKhachHangRequest request);
    XeKhachHangResponse update(Integer id, XeKhachHangRequest request);
    List<XeKhachHangResponse> getAll();
    XeKhachHangResponse getById(Integer id);
    List<XeKhachHangResponse> getByKhachHang(Integer maNguoiDung);
    void delete(Integer id);
}
