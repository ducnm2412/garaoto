package com.example.garaoto.repository;

import com.example.garaoto.entity.DonThueXe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonThueXeRepository extends JpaRepository<DonThueXe, Integer> {
    List<DonThueXe> findByKhachHang_MaNguoiDung(Integer maNguoiDung);
    List<DonThueXe> findByTrangThai(String trangThai);
    List<DonThueXe> findByXeChoThue_MaXeThue(Integer maXeThue);
}
