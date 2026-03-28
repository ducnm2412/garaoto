package com.example.garaoto.service;

import com.example.garaoto.dto.request.PhieuSuaChuaRequest;
import com.example.garaoto.dto.response.PhieuSuaChuaResponse;

import java.util.List;

public interface PhieuSuaChuaService {
    PhieuSuaChuaResponse create(PhieuSuaChuaRequest request);
    List<PhieuSuaChuaResponse> getAll();
    PhieuSuaChuaResponse getById(Integer id);
    List<PhieuSuaChuaResponse> getByKhachHang(Integer maKhachHang);
    PhieuSuaChuaResponse updateStatus(Integer id, String trangThai);
    PhieuSuaChuaResponse updateChanDoan(Integer id, String chanDoan, String trangThai);
}