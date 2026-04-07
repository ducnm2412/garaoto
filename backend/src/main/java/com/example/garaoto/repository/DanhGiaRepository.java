package com.example.garaoto.repository;

import com.example.garaoto.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findByKhachHang_MaKhachHang(Integer maKhachHang);
    List<DanhGia> findByLoaiDanhGia(String loaiDanhGia);
    List<DanhGia> findByDonThueXe_MaDonThue(Integer maDonThue);
    List<DanhGia> findByPhieuSuaChua_MaPhieuSua(Integer maPhieuSua);
}