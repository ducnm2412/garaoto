package com.example.garaoto.repository;

import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    Optional<KhachHang> findByNguoiDung(NguoiDung nguoiDung);
    Optional<KhachHang> findByNguoiDung_MaNguoiDung(Integer maNguoiDung);
}