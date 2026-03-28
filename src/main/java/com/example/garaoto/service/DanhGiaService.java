package com.example.garaoto.service;

import com.example.garaoto.dto.request.DanhGiaRequest;
import com.example.garaoto.dto.response.DanhGiaResponse;

import java.util.List;

public interface DanhGiaService {
    DanhGiaResponse create(DanhGiaRequest request);
    List<DanhGiaResponse> getAll();
    List<DanhGiaResponse> getByKhachHang(Integer maKhachHang);
}