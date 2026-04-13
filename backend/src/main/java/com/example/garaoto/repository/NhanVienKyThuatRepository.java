package com.example.garaoto.repository;

import com.example.garaoto.entity.NguoiDung;
import com.example.garaoto.entity.NhanVienKyThuat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NhanVienKyThuatRepository extends JpaRepository<NhanVienKyThuat, Integer> {

    Optional<NhanVienKyThuat> findById(Integer maNguoiDung);
}
