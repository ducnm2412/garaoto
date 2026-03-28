package com.example.garaoto.repository;

import com.example.garaoto.entity.PhanCongSuaChua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhanCongSuaChuaRepository extends JpaRepository<PhanCongSuaChua, Integer> {
    List<PhanCongSuaChua> findByNhanVienKyThuat_MaNhanVien(Integer maNhanVien);
    List<PhanCongSuaChua> findByAdmin_MaAdmin(Integer maAdmin);
    List<PhanCongSuaChua> findByTrangThai(String trangThai);
    List<PhanCongSuaChua> findByPhieuSuaChua_MaPhieuSua(Integer maPhieuSua);
}