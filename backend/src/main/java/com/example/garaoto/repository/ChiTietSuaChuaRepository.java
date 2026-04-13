package com.example.garaoto.repository;

import com.example.garaoto.entity.ChiTietSuaChua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChiTietSuaChuaRepository extends JpaRepository<ChiTietSuaChua, Integer> {
    List<ChiTietSuaChua> findByPhieuSuaChua_MaPhieuSua(Integer maPhieuSua);
}
