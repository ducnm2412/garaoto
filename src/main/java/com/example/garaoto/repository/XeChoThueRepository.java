package com.example.garaoto.repository;

import com.example.garaoto.entity.XeChoThue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XeChoThueRepository extends JpaRepository<XeChoThue, Integer> {
    List<XeChoThue> findByTinhTrang(String tinhTrang);
    boolean existsByBienSo(String bienSo);
}