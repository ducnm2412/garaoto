package com.example.garaoto.repository;

import com.example.garaoto.entity.Admin;
import com.example.garaoto.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByNguoiDung(NguoiDung nguoiDung);
    Optional<Admin> findByNguoiDung_MaNguoiDung(Integer maNguoiDung);
}