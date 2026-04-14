
package com.example.garaoto.repository;

import com.example.garaoto.entity.PhieuSuaChua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhieuSuaChuaRepository extends JpaRepository<PhieuSuaChua, Integer> {
    List<PhieuSuaChua> findByKhachHang_MaNguoiDung(Integer maNguoiDung);
    List<PhieuSuaChua> findByTrangThai(String trangThai);
    List<PhieuSuaChua> findByXeKhachHang_MaXeKh(Integer maXeKh);
    boolean existsByLichHenSuaChua_MaLichHen(Integer maLichHen);
}
