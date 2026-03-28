package com.example.garaoto.service;

import com.example.garaoto.dto.request.DonThueXeRequest;
import com.example.garaoto.dto.response.DonThueXeResponse;

import java.util.List;

public interface DonThueXeService {
    DonThueXeResponse create(DonThueXeRequest request);
    List<DonThueXeResponse> getAll();
    DonThueXeResponse getById(Integer id);
    List<DonThueXeResponse> getByKhachHang(Integer maKhachHang);
    DonThueXeResponse updateStatus(Integer id, String trangThai);
}