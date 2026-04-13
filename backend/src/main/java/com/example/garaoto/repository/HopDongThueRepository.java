package com.example.garaoto.repository;

import com.example.garaoto.entity.HopDongThue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HopDongThueRepository extends JpaRepository<HopDongThue, Integer> {
    Optional<HopDongThue> findByDonThueXe_MaDonThue(Integer maDonThue);
}
