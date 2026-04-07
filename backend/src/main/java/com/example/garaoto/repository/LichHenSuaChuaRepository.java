package com.example.garaoto.repository;

import com.example.garaoto.entity.LichHenSuaChua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LichHenSuaChuaRepository extends JpaRepository<LichHenSuaChua, Integer> {
    List<LichHenSuaChua> findByKhachHang_MaKhachHang(Integer maKhachHang);
    List<LichHenSuaChua> findByTrangThai(String trangThai);
    List<LichHenSuaChua> findByNgayHen(LocalDate ngayHen);
}