package com.example.garaoto.service;

import com.example.garaoto.dto.response.NguoiDungResponse;

import java.util.List;

public interface NguoiDungService {
    List<NguoiDungResponse> getAll();
    NguoiDungResponse getById(Integer id);
    NguoiDungResponse getByEmail(String email);
    void delete(Integer id);
}