package com.example.garaoto.service;

import com.example.garaoto.dto.request.NhanVienKyThuatRequest;
import com.example.garaoto.dto.response.NhanVienKyThuatResponse;

import java.util.List;

public interface NhanVienKyThuatService {
    List<NhanVienKyThuatResponse> getAll();
    NhanVienKyThuatResponse getById(Integer id);
    NhanVienKyThuatResponse getByNguoiDungId(Integer maNguoiDung);
    NhanVienKyThuatResponse update(Integer id, NhanVienKyThuatRequest request);
    void delete(Integer id);
}
