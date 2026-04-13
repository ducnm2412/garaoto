package com.example.garaoto.service;

import com.example.garaoto.dto.request.DichVuSuaChuaRequest;
import com.example.garaoto.dto.response.DichVuSuaChuaResponse;

import java.util.List;

public interface DichVuSuaChuaService {
    DichVuSuaChuaResponse create(DichVuSuaChuaRequest request);
    DichVuSuaChuaResponse update(Integer id, DichVuSuaChuaRequest request);
    List<DichVuSuaChuaResponse> getAll();
    DichVuSuaChuaResponse getById(Integer id);
    void delete(Integer id);
}
