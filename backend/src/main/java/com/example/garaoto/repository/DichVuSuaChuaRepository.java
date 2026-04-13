package com.example.garaoto.repository;

import com.example.garaoto.entity.DichVuSuaChua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DichVuSuaChuaRepository extends JpaRepository<DichVuSuaChua, Integer> {
    List<DichVuSuaChua> findByTrangThai(String trangThai);
}
