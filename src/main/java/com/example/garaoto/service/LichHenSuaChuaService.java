package com.example.garaoto.service;

import com.example.garaoto.dto.request.LichHenSuaChuaRequest;
import com.example.garaoto.dto.response.LichHenSuaChuaResponse;

import java.util.List;

public interface LichHenSuaChuaService {
    LichHenSuaChuaResponse create(LichHenSuaChuaRequest request);
    List<LichHenSuaChuaResponse> getAll();
    LichHenSuaChuaResponse getById(Integer id);
    List<LichHenSuaChuaResponse> getByKhachHang(Integer maKhachHang);
    LichHenSuaChuaResponse updateStatus(Integer id, String trangThai);
    void delete(Integer id);
}