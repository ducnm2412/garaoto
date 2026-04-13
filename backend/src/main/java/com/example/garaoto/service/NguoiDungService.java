package com.example.garaoto.service;

import com.example.garaoto.dto.response.NguoiDungResponse;

import java.util.List;

public interface NguoiDungService {
    List<NguoiDungResponse> getAll();
    NguoiDungResponse getById(Integer id);
    NguoiDungResponse getByEmail(String email);
    NguoiDungResponse update(Integer id, com.example.garaoto.dto.request.NguoiDungRequest request);
    void delete(Integer id);
}
