package com.example.garaoto.repository;

import com.example.garaoto.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findByKhachHang_MaNguoiDung(Integer maNguoiDung);
    boolean existsByLoaiDichVuAndMaThamChieu(String loaiDichVu, Integer maThamChieu);
    DanhGia findByLoaiDichVuAndMaThamChieu(String loaiDichVu, Integer maThamChieu);
}
