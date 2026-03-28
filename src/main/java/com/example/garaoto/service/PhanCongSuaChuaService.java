package com.example.garaoto.service;

import com.example.garaoto.dto.request.PhanCongSuaChuaRequest;
import com.example.garaoto.dto.response.PhanCongSuaChuaResponse;

import java.util.List;

public interface PhanCongSuaChuaService {
    PhanCongSuaChuaResponse create(PhanCongSuaChuaRequest request);
    List<PhanCongSuaChuaResponse> getAll();
    List<PhanCongSuaChuaResponse> getByNhanVien(Integer maNhanVien);
    PhanCongSuaChuaResponse updateStatus(Integer id, String trangThai);
}