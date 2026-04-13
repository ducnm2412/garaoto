package com.example.garaoto.service;

import com.example.garaoto.dto.request.ChiTietSuaChuaRequest;
import com.example.garaoto.dto.response.ChiTietSuaChuaResponse;

import java.util.List;

public interface ChiTietSuaChuaService {
    ChiTietSuaChuaResponse create(ChiTietSuaChuaRequest request);
    List<ChiTietSuaChuaResponse> getByPhieuSua(Integer maPhieuSua);
    void delete(Integer id);
}
