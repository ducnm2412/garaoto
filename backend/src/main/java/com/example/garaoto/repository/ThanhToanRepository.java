package com.example.garaoto.repository;

import com.example.garaoto.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    List<ThanhToan> findByKhachHang_MaNguoiDung(Integer maNguoiDung);
    List<ThanhToan> findByLoaiThanhToan(String loaiThanhToan);
    List<ThanhToan> findByTrangThai(String trangThai);
    List<ThanhToan> findByDonThueXe_MaDonThue(Integer maDonThue);
    List<ThanhToan> findByPhieuSuaChua_MaPhieuSua(Integer maPhieuSua);
}
