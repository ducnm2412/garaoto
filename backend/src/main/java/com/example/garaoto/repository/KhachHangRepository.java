package com.example.garaoto.repository;

import com.example.garaoto.entity.KhachHang;
import com.example.garaoto.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    Optional<KhachHang> findById(Integer maNguoiDung);
}
