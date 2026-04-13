package com.example.garaoto.service;

import com.example.garaoto.dto.request.HopDongThueRequest;
import com.example.garaoto.dto.response.HopDongThueResponse;

import java.util.List;

public interface HopDongThueService {
    HopDongThueResponse create(HopDongThueRequest request);
    HopDongThueResponse getById(Integer id);
    HopDongThueResponse getByDonThue(Integer maDonThue);
    List<HopDongThueResponse> getAll();
    void delete(Integer id);
}
