package com.example.garaoto.repository;

import com.example.garaoto.entity.XeKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XeKhachHangRepository extends JpaRepository<XeKhachHang, Integer> {
    List<XeKhachHang> findByKhachHang_MaKhachHang(Integer maKhachHang);
    boolean existsByBienSo(String bienSo);
}